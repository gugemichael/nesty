package org.nesty.core.httpserver.rest;

import org.nesty.core.httpserver.rest.response.HttpResponse;

/**
 * Filter of http request and response
 *
 * Author : Michael
 * Date : March 09, 2016
 */
public abstract class HttpInterceptor {

    /**
     * http context of request information. user can update
     * the value of context.
     *
     * @param context http context
     * @return true if we continue the request or false will deny the request by http code 403
     */
    public boolean recvRequest(final HttpContext context) {
        return true;
    }

    /**
     * http context of request information. user can update
     * the value of context or change the response
     *
     * @param context  http context
     * @param response represent response
     * @return response new response instance or current Object instance
     */
    public HttpResponse sendResponse(final HttpContext context, final HttpResponse response) {
        return response;
    }
}
