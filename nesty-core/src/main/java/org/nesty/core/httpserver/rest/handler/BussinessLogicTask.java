package org.nesty.core.httpserver.rest.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.nesty.commons.constant.http.HttpConstants;
import org.nesty.commons.constant.NestyConstants;
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
        HttpResponseStatus status = HttpResponseStatus.OK;

        ByteBuf content = null;
        content = Unpooled.wrappedBuffer(handler.invoke());

        DefaultFullHttpResponse resp;
        if (content != null) {
            resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
            resp.headers().set(HttpConstants.HEADER_CONTENT_LENGTH, content.readableBytes());
        } else
            resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);

        resp.headers().set(HttpConstants.HEADER_SERVER, NestyConstants.NESTY_SERVER);

        return resp;
    }
}
