package org.nesty.core.server.rest;

import org.nesty.core.server.rest.controller.URLController;
import org.nesty.core.server.rest.interceptor.Interceptor;
import org.nesty.core.server.rest.response.ResponseResult;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * nesty
 * <p>
 * Author Michael on 03/03/2016.
 */
public abstract class ExecutorTask<T> implements Callable<T> {

    // Context used for controller invoking
    protected final RequestContext requestContext;
    // related controller
    protected final URLController handler;
    // interceptor list
    protected final List<Interceptor> interceptor;

    public ExecutorTask(RequestContext requestContext, List<Interceptor> interceptor, URLController handler) {
        this.requestContext = requestContext;
        this.interceptor = interceptor;
        this.handler = handler;
    }

    @Override
    public T call() {
        // call interceptor chain of filter
        for (Interceptor every : interceptor) {
            if (!every.filter(requestContext))
                return filterReject();
        }

        // call controller method
        T response = process(handler.call(requestContext));

        // call interceptor chain of *handler*. returned DefaultFullHttpResponse
        // will be replaced to original instance
        for (Interceptor every : interceptor) {
            @SuppressWarnings("unchecked")
            T newResponse = (T) every.handler(requestContext, response);
            // drop null instance
            if (newResponse != null)
                response = newResponse;
        }

        return response;
    }

    protected abstract T filterReject();

    protected abstract T process(ResponseResult result);
}
