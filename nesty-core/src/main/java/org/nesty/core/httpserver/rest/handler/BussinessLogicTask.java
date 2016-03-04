package org.nesty.core.httpserver.rest.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.nesty.core.httpserver.rest.HttpResponseBuilder;
import org.nesty.core.httpserver.rest.route.URLResource;

import java.util.concurrent.Callable;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class BussinessLogicTask implements Callable<DefaultFullHttpResponse> {

    private final URLResource resource;
    private final URLHandler handler;

    public BussinessLogicTask(URLResource resource, URLHandler handler) {
        this.resource = resource;
        this.handler = handler;
    }

    @Override
    public DefaultFullHttpResponse call() {

        // call controller corresponding method
        byte[] result = handler.invoke();

        if (result != null && result.length != 0) {
            ByteBuf content = Unpooled.wrappedBuffer(result);
            return HttpResponseBuilder.create(content);
        } else
            return HttpResponseBuilder.create(HttpResponseStatus.NO_CONTENT);
    }
}
