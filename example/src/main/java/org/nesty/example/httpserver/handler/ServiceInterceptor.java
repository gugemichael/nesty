package org.nesty.example.httpserver.handler;

import org.nesty.commons.annotations.Interceptor;
import org.nesty.core.httpserver.rest.HttpContext;
import org.nesty.core.httpserver.rest.HttpInterceptor;
import org.nesty.core.httpserver.rest.response.HttpResponse;

@Interceptor
public class ServiceInterceptor extends HttpInterceptor {

    @Override
    public boolean recvRequest(final HttpContext context) {
//        System.out.println(context.getRemoteAddress());
        return true;
    }

    @Override
    public HttpResponse sendResponse(final HttpContext context, final HttpResponse response) {
//        System.out.println(response.getHttpStatus());
        return response;
    }
}
