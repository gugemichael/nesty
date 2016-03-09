package org.nesty.core.httpserver.impl.async;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.ReadTimeoutException;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.core.httpserver.rest.HttpContext;
import org.nesty.core.httpserver.rest.URLHandler;
import org.nesty.core.httpserver.rest.URLResource;
import org.nesty.core.httpserver.rest.executor.ExecutorTask;
import org.nesty.core.httpserver.rest.request.NettyHttpRequestVisitor;
import org.nesty.core.httpserver.rest.response.HttpResponseBuilder;
import org.nesty.core.httpserver.utils.HttpUtils;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class AsyncRequestRouter extends AsyncRequestReceiver {

    // channel context
    private ChannelHandlerContext context;
    // channel http request struct
    private FullHttpRequest httpRequest;

    public static AsyncRequestRouter build() {
        return new AsyncRequestRouter();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest httpRequest) throws Exception {
        this.context = ctx;
        this.httpRequest = httpRequest;

        // execute logic code
        doRun();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            // httpcode 504
            if (context.channel().isOpen())
                writeAndClose(HttpResponseBuilder.create(HttpResponseStatus.GATEWAY_TIMEOUT));
        }
    }

    private void doRun() {

        /**
         * 1. checking phase. http method, param, url
         */
        if (!checkup())
            return;

        /**
         * 2. according to URL to search the URLHandler
         */
        URLHandler handler = findURLHandler();

        /**
         * 3. execute controller logic to async executor thread pool
         */
        if (handler != null)
            executeAsyncTask(handler);
    }

    private boolean checkup() {
        if (HttpUtils.convertHttpMethodFromNetty(httpRequest) == RequestMethod.UNKOWN) {
            // httpcode 405
            writeAndClose(HttpResponseBuilder.create(HttpResponseStatus.METHOD_NOT_ALLOWED));
            return false;
        }
        return true;
    }

    private URLHandler findURLHandler() {
        // build URLResource from incoming http request
        URLResource resource = URLResource.fromHttp(httpRequest.getUri(), HttpUtils.convertHttpMethodFromNetty(httpRequest));
        URLHandler handler;
        if ((handler = routeController.findURLHandler(resource)) == null) {
            // httpcode 404
            writeAndClose(HttpResponseBuilder.create(HttpResponseStatus.NOT_FOUND));
        }
        return handler;
    }

    private void executeAsyncTask(URLHandler handler) {
        // build logic task
        ExecutorTask task = new ExecutorTask(HttpContext.build(new NettyHttpRequestVisitor(context.channel(), httpRequest)), interceptor, handler);

        Futures.addCallback(taskWorkerPool.submit(task), new FutureCallback<DefaultFullHttpResponse>() {
            @Override
            public void onSuccess(DefaultFullHttpResponse resp) {
                writeAndClose(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                e.printStackTrace();
                // httpcode 503
                writeAndClose(HttpResponseBuilder.create(HttpResponseStatus.SERVICE_UNAVAILABLE));
            }
        });
    }

    private void writeAndClose(DefaultFullHttpResponse response) {
        context.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
