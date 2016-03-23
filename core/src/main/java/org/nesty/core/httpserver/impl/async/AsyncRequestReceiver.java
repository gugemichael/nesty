package org.nesty.core.httpserver.impl.async;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.nesty.core.httpserver.rest.HttpInterceptor;
import org.nesty.core.httpserver.rest.route.RouteControlloer;

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
    protected static volatile RouteControlloer routeController;

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

    public static void newURLResourceController(RouteControlloer routeControllerMap) {
        routeController = routeControllerMap;
    }

    public static void newInterceptor(List<HttpInterceptor> interceptorList) {
        interceptor = interceptorList;
    }
}
