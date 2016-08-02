package org.nesty.core.server.acceptor;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.nesty.core.server.NestyServerMonitor;
import org.nesty.core.server.rest.interceptor.Interceptor;
import org.nesty.core.server.rest.ControllerRouter;

import java.util.List;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public abstract class AsyncRequestReceiver extends SimpleChannelInboundHandler<FullHttpRequest> {

    // resource controller route mapping
    protected static volatile ControllerRouter controllerRouter;
    // resource controller route mapping
    protected static volatile List<Interceptor> interceptor;
    // Async workers
    protected static volatile ListeningExecutorService ioWorker;

    public static void newTaskPool(int workers) {
        if (workers >= 0)
            ioWorker = MoreExecutors.listeningDecorator(IoWorker.newExecutors(workers));
    }

    public static void useURLResourceController(ControllerRouter routeControllerMap) {
        controllerRouter = routeControllerMap;
    }

    public static void useInterceptor(List<Interceptor> interceptorList) {
        interceptor = interceptorList;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        NestyServerMonitor.incrConnections();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        NestyServerMonitor.decrConnections();
    }
}
