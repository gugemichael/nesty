package org.nesty.core.httpserver.impl.async;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.nesty.core.httpserver.HttpServerStats;
import org.nesty.core.httpserver.rest.interceptor.HttpInterceptor;
import org.nesty.core.httpserver.rest.ControllerRouter;

import java.util.List;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public abstract class AsyncRequestReceiver extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * resource controller route mapping
     */
    protected static volatile ControllerRouter controllerRouter;

    /**
     * resource controller route mapping
     */
    protected static volatile List<HttpInterceptor> interceptor;

    /**
     * Async workers
     */
    protected static volatile ListeningExecutorService taskWorkerPool;

    public static void newTaskPool(int workers) {
        taskWorkerPool = MoreExecutors.listeningDecorator(AsyncExecutors.newExecutors(workers));
    }

    public static void useURLResourceController(ControllerRouter routeControllerMap) {
        controllerRouter = routeControllerMap;
    }

    public static void useInterceptor(List<HttpInterceptor> interceptorList) {
        interceptor = interceptorList;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        HttpServerStats.CONNECTIONS.incrementAndGet();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        HttpServerStats.CONNECTIONS.decrementAndGet();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
    }
}
