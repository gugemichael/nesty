package org.nesty.example.httpserver.handler;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import org.nesty.commons.annotations.Interceptor;
import org.nesty.core.server.rest.HttpContext;

@Interceptor
public class ServiceQPSInterceptor extends org.nesty.core.server.rest.interceptor.Interceptor {

    @Override
    public boolean filter(final HttpContext context) {
        return true;
    }

    @Override
    public DefaultFullHttpResponse handler(final HttpContext context, final DefaultFullHttpResponse response) {
        return response;
    }
}
