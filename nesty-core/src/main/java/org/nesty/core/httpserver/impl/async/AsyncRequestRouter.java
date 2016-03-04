package org.nesty.core.httpserver.impl.async;

import com.google.common.util.concurrent.*;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.timeout.ReadTimeoutException;
import org.nesty.core.httpserver.HttpServer;
import org.nesty.core.httpserver.rest.NettyHttpRequestVisitor;
import org.nesty.core.httpserver.rest.handler.BussinessLogicTask;
import org.nesty.core.httpserver.rest.handler.URLHandler;
import org.nesty.core.httpserver.rest.route.URLResource;
import org.nesty.core.httpserver.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class AsyncRequestRouter extends SimpleChannelInboundHandler<FullHttpRequest> {

//    private static final ByteBuf error = Unpooled.wrappedBuffer("404".getBytes());

    /**
     * Restful mapping
     */
    private static final Map<URLResource, URLHandler> routeTable = new HashMap<>(128);
    /**
     * Async workers
     */
    private static ListeningExecutorService taskWorkerPool;

    public static void newTaskPool(int workers) {
        taskWorkerPool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(workers));
    }

    public static AsyncRequestRouter build(HttpServer httpServer) {
        AsyncRequestRouter router = new AsyncRequestRouter();
        return router;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest httpRequest) throws Exception {
        System.err.println("---------------");
        URLResource resource = URLResource.fromHttp(httpRequest.getUri(), HttpUtils.convertHttpMethodFromNetty(httpRequest));
        URLHandler handler = null;
        if ((handler = routeTable.get(resource)) != null) {
            // found url pattern handler
            ListenableFuture<DefaultFullHttpResponse> task = taskWorkerPool.submit(
                                                                                        new BussinessLogicTask(
                                                                                            resource.parse(new NettyHttpRequestVisitor(ctx.channel(), httpRequest)), handler)
                                                                                        );
            Futures.addCallback(task, new FutureCallback<DefaultFullHttpResponse>(){
                @Override
                public void onSuccess(DefaultFullHttpResponse resp) {
                    ctx.channel().writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    ctx.channel().writeAndFlush(errorResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR)).addListener(ChannelFutureListener.CLOSE);
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
       return new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, status);
    }
}
