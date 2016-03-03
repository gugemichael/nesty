package org.nesty.core.httpserver.utils;

import io.netty.handler.codec.http.FullHttpRequest;
import org.nesty.commons.constant.http.HttpMethod;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class HttpUtils {

    public static HttpMethod convertHttpMethodFromNetty(FullHttpRequest request) {
        try {
            return HttpMethod.valueOf(request.getMethod().name().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return HttpMethod.UNKOWN;
        }
    }
}
