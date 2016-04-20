package org.nesty.core.server.acceptor.http;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.nesty.commons.constant.RequestMethod;
import org.nesty.core.server.NestyServerMonitor;
import org.nesty.core.server.acceptor.AsyncRequestDispatcher;
import org.nesty.core.server.rest.ExecutorTask;
import org.nesty.core.server.rest.RequestContext;
import org.nesty.core.server.rest.URLResource;
import org.nesty.core.server.rest.controller.URLController;

/**
 * nesty
 * <p>
 * Author Michael on 03/03/2016.
 */
public class AsyncHttpHandler extends AsyncRequestDispatcher<FullHttpRequest> {

    // Http context
    private volatile RequestContext requestContext;
    // channel context
    private ChannelHandlerContext context;

    static AsyncHttpHandler build() {
        return new AsyncHttpHandler();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest httpRequest) throws Exception {
        // make context
        this.context = ctx;
        this.requestContext = RequestContext.build(new HttpRequestVisitor(context.channel(), httpRequest));

        super.channelRead0(ctx, httpRequest);
    }

    @Override
    protected void handleTimeout() {
        // httpcode 504
        if (context != null && context.channel().isOpen())
            sendResponse(requestContext, HttpResponseBuilder.create(requestContext, HttpResponseStatus.GATEWAY_TIMEOUT));
    }

    @Override
    protected boolean checkup() {
        if (requestContext.getRequestMethod() == RequestMethod.UNKOWN) {
            // httpcode 405
            sendResponse(requestContext, HttpResponseBuilder.create(requestContext, HttpResponseStatus.METHOD_NOT_ALLOWED));
            return false;
        }

        return true;
    }

    @Override
    protected URLController findController() {
        // build URLResource from incoming http request
        URLResource resource = URLResource.fromUri(requestContext.getUri(), requestContext.getRequestMethod());
        URLController controller;
        if ((controller = controllerRouter.findURLController(resource)) == null) {
            // httpcode 404
            sendResponse(requestContext, HttpResponseBuilder.create(requestContext, HttpResponseStatus.NOT_FOUND));
            NestyServerMonitor.incrRequestMiss();
            return null;
        }

        return controller;
    }

    @Override
    protected void execute(URLController controller) {
        // if IOWorker pool is empty. we use this same thread directly.
        // no more context switch (queue pop/push cause) happen here !
        if (ioWorker == null) {
            try {
                sendResponse(requestContext, new HttpExecutableTask(requestContext, interceptor, controller).call());
            } catch (Throwable e) {
                e.printStackTrace();
                sendResponse(requestContext, failure());
            }
            return;
        }

        executeAsyncTask(new HttpExecutableTask(requestContext, interceptor, controller));
    }

    private void executeAsyncTask(ExecutorTask task) {
        final RequestContext currentContext = requestContext;

        Futures.addCallback(ioWorker.submit(task), new FutureCallback<DefaultFullHttpResponse>() {
            @Override
            public void onSuccess(DefaultFullHttpResponse resp) {
                sendResponse(currentContext, resp);
            }

            @Override
            public void onFailure(Throwable e) {
                e.printStackTrace();
                sendResponse(currentContext, failure());
            }
        });
    }

    private DefaultFullHttpResponse failure() {
        NestyServerMonitor.setLastServFailID(requestContext.getRequestId());
        // httpcode 503
        return HttpResponseBuilder.create(requestContext, HttpResponseStatus.SERVICE_UNAVAILABLE);
    }


    private void sendResponse(RequestContext ctx, DefaultFullHttpResponse response) {
        ChannelFuture future = context.channel().writeAndFlush(response);

        // http short connection
        if (!ctx.isKeepAlive())
            future.addListener(ChannelFutureListener.CLOSE);
    }
}
