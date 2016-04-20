package org.nesty.core.server.acceptor.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.nesty.commons.utils.SerializeUtils;
import org.nesty.core.server.rest.ExecutorTask;
import org.nesty.core.server.rest.RequestContext;
import org.nesty.core.server.rest.controller.URLController;
import org.nesty.core.server.rest.interceptor.Interceptor;
import org.nesty.core.server.rest.response.ResponseResult;

import java.util.List;

/**
 * nesty
 * <p>
 * Author Michael on 03/03/2016.
 */
class HttpExecutableTask extends ExecutorTask<DefaultFullHttpResponse> {

    HttpExecutableTask(RequestContext requestContext, List<Interceptor> interceptor, URLController handler) {
        super(requestContext, interceptor, handler);
    }

    @Override
    protected DefaultFullHttpResponse filterReject() {
        return HttpResponseBuilder.create(requestContext, HttpResponseStatus.FORBIDDEN);                            // httpcode 403
    }

    @Override
    protected DefaultFullHttpResponse process(ResponseResult result) {
        DefaultFullHttpResponse response;

        switch (result.getStatus()) {
        case SUCCESS:
            if (result.getBody() != null) {
                ByteBuf content = Unpooled.wrappedBuffer(SerializeUtils.encode(result.getBody()));
                response = HttpResponseBuilder.create(requestContext, content);                                              // httpcode 200
            } else
                response = HttpResponseBuilder.create(requestContext, HttpResponseStatus.NO_CONTENT);          // httpcode 204
            break;
        case RESPONSE_NOT_VALID:
            response = HttpResponseBuilder.create(requestContext, HttpResponseStatus.BAD_GATEWAY);            // httpcode 502
            break;
        case PARAMS_CONVERT_ERROR:
        case PARAMS_NOT_MATCHED:
            response = HttpResponseBuilder.create(requestContext, HttpResponseStatus.BAD_REQUEST);             // httpcode 400
            break;
        case SYSTEM_ERROR:
            response = HttpResponseBuilder.create(requestContext, HttpResponseStatus.INTERNAL_SERVER_ERROR);    // httpcode 500
            break;
        default:
            response = HttpResponseBuilder.create(requestContext, HttpResponseStatus.INTERNAL_SERVER_ERROR);    // httpcode 500
            break;
        }

        return response;
    }
}
