package org.nesty.core.server.acceptor;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import org.nesty.core.server.NestyServerMonitor;
import org.nesty.core.server.rest.ControllerRouter;
import org.nesty.core.server.rest.controller.URLController;
import org.nesty.core.server.rest.interceptor.Interceptor;

import java.util.List;

/**
 * nesty
 * <p>
 * Author Michael on 03/03/2016.
 */
public abstract class AsyncRequestDispatcher<T> extends SimpleChannelInboundHandler<T> {

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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException)
            handleTimeout();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final T request) throws Exception {
        // execute async logic code block
        doRun();
    }

    private void doRun() {

        /**
         * 1. checking phase. http method, param, url
         *
         */
        if (!checkup())
            return;

        /**
         * 2. find out URLController by URL and params
         *
         */
        URLController controller = findController();

        /**
         * 3. execute controller logic to async executor thread pool
         *
         */
        if (controller == null)
            return;

        if (!controller.isInternal()) {
            NestyServerMonitor.incrRequestHit();
            controller.hit();
        }

        execute(controller);
    }

    protected abstract boolean checkup();

    protected abstract URLController findController();

    protected abstract void execute(URLController controller);

    protected abstract void handleTimeout();
}
