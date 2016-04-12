package org.nesty.core.httpserver.rest.interceptor;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import org.nesty.commons.constant.NestyConstants;
import org.nesty.commons.constant.http.HttpConstants;
import org.nesty.core.httpserver.rest.HttpContext;

/**
 * Response updater Interceptor
 *
 * Author : Michael
 * Date : March 09, 2016
 *
 */
public class DefaultInterceptor extends HttpInterceptor {

    /**
     * modify http response header
     *
     */
    public DefaultFullHttpResponse sendResponse(final HttpContext context, final DefaultFullHttpResponse response) {
        response.headers().set(HttpConstants.HEADER_SERVER, NestyConstants.NESTY_SERVER);
        response.headers().set(HttpConstants.HEADER_REQUEST_ID, context.getRequestId());
        response.headers().set(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.HEADER_CONTENT_TYPE_JSON);
        response.headers().set(HttpConstants.HEADER_CONNECTION, HttpConstants.HEADER_CONNECTION_KEEPALIVE);
        return response;
    }
}
