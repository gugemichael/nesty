package org.nesty.core.httpserver.rest.executor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.nesty.commons.utils.SerializeUtils;
import org.nesty.core.httpserver.rest.HttpContextInterceptor;
import org.nesty.core.httpserver.rest.response.HttpResponse;
import org.nesty.core.httpserver.rest.response.HttpResponseBuilder;
import org.nesty.core.httpserver.rest.HttpContext;
import org.nesty.core.httpserver.rest.URLHandler;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class ExecutorTask implements Callable<DefaultFullHttpResponse> {

    private final URLHandler handler;
    private final HttpContext context;
    private final List<HttpContextInterceptor> interceptor;

    public ExecutorTask(HttpContext context, List<HttpContextInterceptor> interceptor, URLHandler handler) {
        this.context = context;
        this.interceptor = interceptor;
        this.handler = handler;
    }

    @Override
    public DefaultFullHttpResponse call() {
        // call interceptor chain of recvRequest
        for (HttpContextInterceptor every : interceptor) {
            if (!every.recvRequest(context))
                return HttpResponseBuilder.create(HttpResponseStatus.FORBIDDEN);        // httpcode 403
        }

        // call controller method
        HttpResponse result = handler.call(context);

        // call interceptor chain of sendResponse
        for (HttpContextInterceptor every : interceptor)
            result = every.sendResponse(context, result);

        switch (result.getHttpStatus()) {
        case SUCCESS:
            if (result.getHttpContent() != null) {
                ByteBuf content = Unpooled.wrappedBuffer(SerializeUtils.encode(result.getHttpContent()));
                return HttpResponseBuilder.create(content);                         // httpcode 200
            } else
                return HttpResponseBuilder.create(HttpResponseStatus.NO_CONTENT);       // httpcode 204
        case RESPONSE_NOT_VALID:
            return HttpResponseBuilder.create(HttpResponseStatus.BAD_GATEWAY);        // httpcode 502
        case PARAMS_CONVERT_ERROR:
        case PARAMS_NOT_MATCHED:
            return HttpResponseBuilder.create(HttpResponseStatus.BAD_REQUEST);         // httpcode 400
        case SYSTEM_ERROR:
            return HttpResponseBuilder.create(HttpResponseStatus.INTERNAL_SERVER_ERROR);    // httpcode 500
        default:
            return HttpResponseBuilder.create(HttpResponseStatus.INTERNAL_SERVER_ERROR);    // httpcode 500
        }

    }
}
