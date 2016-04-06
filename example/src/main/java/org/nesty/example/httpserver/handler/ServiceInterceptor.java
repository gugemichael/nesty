package org.nesty.example.httpserver.handler;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import org.nesty.commons.annotations.Interceptor;
import org.nesty.core.httpserver.rest.HttpContext;
import org.nesty.core.httpserver.rest.interceptor.HttpInterceptor;

@Interceptor
public class ServiceInterceptor extends HttpInterceptor {

    @Override
    public boolean recvRequest(final HttpContext context) {
//        System.out.println(context.getRemoteAddress());
        return true;
    }

    @Override
    public DefaultFullHttpResponse sendResponse(final HttpContext context, final DefaultFullHttpResponse response) {
//        System.out.println(response.getHttpStatus());
        return response;
    }
}
