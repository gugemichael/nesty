package org.nesty.core.httpserver.impl.async;

import com.google.common.util.concurrent.*;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.ReadTimeoutException;
import org.nesty.core.httpserver.HttpServer;
import org.nesty.core.httpserver.rest.HttpResponseBuilder;
import org.nesty.core.httpserver.rest.NettyHttpRequestVisitor;
import org.nesty.core.httpserver.rest.URLContext;
import org.nesty.core.httpserver.rest.handler.BussinessLogicTask;
import org.nesty.core.httpserver.rest.handler.URLHandler;
import org.nesty.core.httpserver.rest.route.RouteControlloer;
import org.nesty.core.httpserver.rest.route.URLResource;
import org.nesty.core.httpserver.utils.HttpUtils;

import java.util.concurrent.Executors;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class AsyncRequestRouter extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * resource controller route mapping
     */
    private static volatile RouteControlloer routeController;

    /**
     * Async workers
     */
    private static ListeningExecutorService taskWorkerPool;

    public static void newTaskPool(int workers) {
        taskWorkerPool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(workers));
    }

    public static void newURLResourceController(RouteControlloer routeControllerMap) {
        routeController = routeControllerMap;
    }

    public static AsyncRequestRouter build(HttpServer httpServer) {
        AsyncRequestRouter router = new AsyncRequestRouter();
        return router;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest httpRequest) throws Exception {
        URLResource resource = URLResource.fromHttp(httpRequest.getUri(), HttpUtils.convertHttpMethodFromNetty(httpRequest));
        URLHandler handler = null;
        if ((handler = routeController.findURLHandler(resource)) != null) {
            // found url pattern handler
            BussinessLogicTask task = new BussinessLogicTask(handler, URLContext.build(new NettyHttpRequestVisitor(ctx.channel(), httpRequest)));
            ListenableFuture<DefaultFullHttpResponse> futureTask = taskWorkerPool.submit(task);
            Futures.addCallback(futureTask, new FutureCallback<DefaultFullHttpResponse>() {
                @Override
                public void onSuccess(DefaultFullHttpResponse resp) {
                    ctx.channel().writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
                }

                @Override
                public void onFailure(Throwable e) {
                    e.printStackTrace();
                    ctx.channel().writeAndFlush(errorResponse(HttpResponseStatus.SERVICE_UNAVAILABLE)).addListener(ChannelFutureListener.CLOSE);
                }
            });
        } else {
            ctx.channel().writeAndFlush(errorResponse(HttpResponseStatus.NOT_FOUND)).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            ctx.channel().writeAndFlush(errorResponse(HttpResponseStatus.GATEWAY_TIMEOUT)).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private DefaultFullHttpResponse errorResponse(HttpResponseStatus status) {
        return HttpResponseBuilder.create(status);
    }
}
