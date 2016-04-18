package org.nesty.core.server.rest.interceptor;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import org.nesty.core.server.rest.HttpContext;

/**
 * Filter of http request and response
 *
 * Author : Michael
 * Date : March 09, 2016
 */
public abstract class Interceptor {

    /**
     * http context of request information. user can update
     * the value of context.
     *
     * @param context http context
     * @return true if we continue the request or false will deny the request by http code 403
     */
    public boolean filter(final HttpContext context) {
        return true;
    }

    /**
     * http context of request information. user can update
     * the value of context or change the response. don't accept null
     * returned. it will be ignored
     *
     * @param context  http context
     * @param response represent response
     * @return response new response instance or current Object instance
     */
    public DefaultFullHttpResponse handler(final HttpContext context, DefaultFullHttpResponse response) {
        return response;
    }
}
