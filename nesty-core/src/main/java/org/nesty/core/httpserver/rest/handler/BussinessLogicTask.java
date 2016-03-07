package org.nesty.core.httpserver.rest.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.nesty.commons.utils.Tuple;
import org.nesty.core.httpserver.impl.async.HttpResultStatus;
import org.nesty.core.httpserver.rest.HttpResponseBuilder;
import org.nesty.core.httpserver.rest.URLContext;

import java.util.concurrent.Callable;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class BussinessLogicTask implements Callable<DefaultFullHttpResponse> {

    private final URLHandler handler;
    private final URLContext context;

    public BussinessLogicTask(URLHandler handler, URLContext context) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public DefaultFullHttpResponse call() {

        // call controller corresponding method
        Tuple<HttpResultStatus, byte[]> result = handler.call(context);

        switch (result.first) {
        case SUCCESS:
            if (result.second != null && result.second.length != 0) {
                ByteBuf content = Unpooled.wrappedBuffer(result.second);
                return HttpResponseBuilder.create(content);
            } else
                return HttpResponseBuilder.create(HttpResponseStatus.NO_CONTENT);
        case RESPONSE_NOT_VALID:
            return HttpResponseBuilder.create(HttpResponseStatus.NOT_EXTENDED);
        case PARAMS_CONVERT_ERROR:
        case PARAMS_NOT_MATCHED:
            return HttpResponseBuilder.create(HttpResponseStatus.BAD_REQUEST);
        case SYSTEM_ERROR:
            return HttpResponseBuilder.create(HttpResponseStatus.BAD_GATEWAY);
        default:
            return HttpResponseBuilder.create(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
