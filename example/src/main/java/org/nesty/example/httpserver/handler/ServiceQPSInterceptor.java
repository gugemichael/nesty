package org.nesty.example.httpserver.handler;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import org.nesty.commons.annotations.Interceptor;
import org.nesty.core.server.NestyOptionProvider;
import org.nesty.core.server.rest.HttpContext;

@Interceptor
public class ServiceQPSInterceptor extends org.nesty.core.server.rest.interceptor.Interceptor {

    @Override
    public boolean install(NestyOptionProvider nesty) {
        return super.install(nesty);
    }

    @Override
    public boolean filter(HttpContext context) {
        return super.filter(context);
    }

    @Override
    public DefaultFullHttpResponse handler(HttpContext context, DefaultFullHttpResponse response) {
        return super.handler(context, response);
    }
}
