package org.nesty.core.httpserver.impl.async;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.ReadTimeoutException;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.core.httpserver.HttpServerStats;
import org.nesty.core.httpserver.rest.ExecutorTask;
import org.nesty.core.httpserver.rest.HttpContext;
import org.nesty.core.httpserver.rest.URLResource;
import org.nesty.core.httpserver.rest.controller.URLController;
import org.nesty.core.httpserver.rest.request.NettyHttpRequestVisitor;
import org.nesty.core.httpserver.rest.response.HttpResponseBuilder;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class AsyncRequestRouter extends AsyncRequestReceiver {

    // Http context
    HttpContext httpContext;
    // channel context
    private ChannelHandlerContext context;

    public static AsyncRequestRouter build() {
        return new AsyncRequestRouter();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest httpRequest) throws Exception {
        this.context = ctx;

        // build context
        httpContext = HttpContext.build(new NettyHttpRequestVisitor(context.channel(), httpRequest));

        // execute async logic code block
        doRun();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            // httpcode 504
            if (context.channel().isOpen())
                sendResponse(HttpResponseBuilder.create(HttpResponseStatus.GATEWAY_TIMEOUT));
        }
    }

    private void doRun() {

        /**
         * 1. checking phase. http method, param, url
         *
         */
        if (!checkup())
            return;

        /**
         * 2. according to URL to search the URLController
         *
         */
        URLController controller = findController();

        /**
         * 3. execute controller logic to async executor thread pool
         *
         */
        if (controller != null)
            executeAsyncTask(controller);
    }

    private boolean checkup() {
        // check http method
        if (httpContext.getRequestMethod() == RequestMethod.UNKOWN) {
            // httpcode 405
            sendResponse(HttpResponseBuilder.create(HttpResponseStatus.METHOD_NOT_ALLOWED));
            return false;
        }

        return true;
    }

    private URLController findController() {
        // build URLResource from incoming http request
        URLResource resource = URLResource.fromHttp(httpContext.getUri(), httpContext.getRequestMethod());
        URLController controller;
        if ((controller = controllerRouter.findURLController(resource)) == null) {
            // httpcode 404
            sendResponse(HttpResponseBuilder.create(HttpResponseStatus.NOT_FOUND));
            HttpServerStats.incrRequestMiss();
            return null;
        }

        if (!controller.isInternal()) {
            HttpServerStats.incrRequestHit();
            controller.hit();
        }

        return controller;
    }

    private void executeAsyncTask(URLController controller) {
        if (!controller.isInternal()) {
            HttpServerStats.setLastServTime(System.currentTimeMillis());
            HttpServerStats.setLastServID(httpContext.getRequestId());
        }

        // build logic task
        ExecutorTask task = new ExecutorTask(httpContext, interceptor, controller);

        Futures.addCallback(taskWorkerPool.submit(task), new FutureCallback<DefaultFullHttpResponse>() {
            @Override
            public void onSuccess(DefaultFullHttpResponse resp) {
                sendResponse(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                e.printStackTrace();
                HttpServerStats.setLastServFailID(httpContext.getRequestId());
                // httpcode 503
                sendResponse(HttpResponseBuilder.create(HttpResponseStatus.SERVICE_UNAVAILABLE));
            }
        });
    }

    private void sendResponse(DefaultFullHttpResponse response) {
        ChannelFuture future = context.channel().writeAndFlush(response);

        // http short connection
        if (!httpContext.isKeepAlive())
            future.addListener(ChannelFutureListener.CLOSE);
    }
}
