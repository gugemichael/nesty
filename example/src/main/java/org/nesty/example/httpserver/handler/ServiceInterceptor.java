package org.nesty.example.httpserver.handler;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import org.nesty.commons.annotations.Interceptor;
import org.nesty.core.server.rest.RequestContext;
import org.nesty.core.server.rest.interceptor.HttpInterceptor;

@Interceptor
public class ServiceInterceptor extends HttpInterceptor {

    @Override
    public boolean filter(final RequestContext context) {
        return true;
    }

    @Override
    public DefaultFullHttpResponse handler(final RequestContext context, DefaultFullHttpResponse response) {
        System.out.println("handler uri " + context.getUri());
        return response;
    }
}
