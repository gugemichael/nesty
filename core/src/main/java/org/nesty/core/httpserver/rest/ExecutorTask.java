package org.nesty.core.httpserver.rest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.nesty.commons.utils.SerializeUtils;
import org.nesty.core.httpserver.rest.HttpContext;
import org.nesty.core.httpserver.rest.interceptor.HttpInterceptor;
import org.nesty.core.httpserver.rest.controller.URLController;
import org.nesty.core.httpserver.rest.response.HttpResponse;
import org.nesty.core.httpserver.rest.response.HttpResponseBuilder;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class ExecutorTask implements Callable<DefaultFullHttpResponse> {

    private final URLController handler;
    private final HttpContext context;
    private final List<HttpInterceptor> interceptor;

    public ExecutorTask(HttpContext context, List<HttpInterceptor> interceptor, URLController handler) {
        this.context = context;
        this.interceptor = interceptor;
        this.handler = handler;
    }

    @Override
    public DefaultFullHttpResponse call() {
        // call interceptor chain of recvRequest
        for (HttpInterceptor every : interceptor) {
            if (!every.recvRequest(context))
                return HttpResponseBuilder.create(HttpResponseStatus.FORBIDDEN);        // httpcode 403
        }

        // call controller method
        HttpResponse result = handler.call(context);

        DefaultFullHttpResponse response;

        switch (result.getHttpStatus()) {
        case SUCCESS:
            if (result.getHttpContent() != null) {
                ByteBuf content = Unpooled.wrappedBuffer(SerializeUtils.encode(result.getHttpContent()));
                response = HttpResponseBuilder.create(content);                         // httpcode 200
            } else
                response = HttpResponseBuilder.create(HttpResponseStatus.NO_CONTENT);       // httpcode 204
            break;
        case RESPONSE_NOT_VALID:
            response = HttpResponseBuilder.create(HttpResponseStatus.BAD_GATEWAY);        // httpcode 502
            break;
        case PARAMS_CONVERT_ERROR:
        case PARAMS_NOT_MATCHED:
            response = HttpResponseBuilder.create(HttpResponseStatus.BAD_REQUEST);         // httpcode 400
            break;
        case SYSTEM_ERROR:
            response = HttpResponseBuilder.create(HttpResponseStatus.INTERNAL_SERVER_ERROR);    // httpcode 500
            break;
        default:
            response = HttpResponseBuilder.create(HttpResponseStatus.INTERNAL_SERVER_ERROR);    // httpcode 500
            break;
        }

        // call interceptor chain of sendResponse
        for (HttpInterceptor every : interceptor) {
            DefaultFullHttpResponse newResponse = every.sendResponse(context, response);
            if (newResponse != null)
                response = newResponse;
        }

        return response;
    }

}
