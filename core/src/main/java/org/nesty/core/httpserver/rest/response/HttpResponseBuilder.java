package org.nesty.core.httpserver.rest.response;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.nesty.commons.constant.NestyConstants;
import org.nesty.commons.constant.http.HttpConstants;
import org.nesty.commons.utils.SerializeUtils;
import org.nesty.core.httpserver.rest.HttpContext;

/**
 * Build an DefaultFullHttpResponse {@link DefaultFullHttpResponse}
 *
 * [Author] Michael
 * [Date] March 04, 2016
 */
public class HttpResponseBuilder {

    // if no content in DefaultFullHttpResponse we will fill empty body
    private static final byte[] uselessBuffer = SerializeUtils.encode(new EMPTY());

    public static DefaultFullHttpResponse create(HttpContext httpContext, HttpResponseStatus status) {
        return create(httpContext, status, Unpooled.wrappedBuffer(uselessBuffer));
    }

    public static DefaultFullHttpResponse create(HttpContext httpContext, ByteBuf content) {
        return create(httpContext, HttpResponseStatus.OK, content);
    }

    public static DefaultFullHttpResponse create(HttpContext httpContext, HttpResponseStatus status, ByteBuf content) {
        DefaultFullHttpResponse resp;
        if (content != null) {
            resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
            resp.headers().set(HttpConstants.HEADER_CONTENT_LENGTH, content.readableBytes());
            resp.headers().set(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.HEADER_CONTENT_TYPE_JSON);
        } else
            resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);

        // fill nesty headers
        resp.headers().set(HttpConstants.HEADER_REQUEST_ID, httpContext.getRequestId());
        resp.headers().set(HttpConstants.HEADER_SERVER, NestyConstants.NESTY_SERVER);
        resp.headers().set(HttpConstants.HEADER_CONNECTION, HttpConstants.HEADER_CONNECTION_KEEPALIVE);

        return resp;
    }

    static class EMPTY {
    }
}
