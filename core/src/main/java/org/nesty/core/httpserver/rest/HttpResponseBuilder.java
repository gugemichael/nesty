package org.nesty.core.httpserver.rest;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.nesty.commons.constant.NestyConstants;
import org.nesty.commons.constant.http.HttpConstants;

/**
 * Build an HttpResponse
 *
 * [Author] Michael
 * [Date] March 04, 2016
 */
public class HttpResponseBuilder {

    public static DefaultFullHttpResponse create(HttpResponseStatus status) {
        return create(status, null);
    }

    public static DefaultFullHttpResponse create(ByteBuf content) {
        return create(HttpResponseStatus.OK, content);
    }

    public static DefaultFullHttpResponse create(HttpResponseStatus status, ByteBuf content) {
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
