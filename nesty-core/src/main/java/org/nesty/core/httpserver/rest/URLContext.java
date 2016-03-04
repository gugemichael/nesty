package org.nesty.core.httpserver.rest;

import org.nesty.commons.constant.http.HttpMethod;

import java.util.Map;
import java.util.UUID;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class URLContext {

    /**
     * client request with unique id, fetch it from http header(Request-Id)
     * if exsit. or generate by UUID
     */
    public String requestId = UUID.randomUUID().toString();

    /**
     * ip address from origin client, fetch it from getRemoteAddr()
     * or header X-FORWARDED-FOR
     */
    public String remoteAddress;

    /**
     * Http request method. can't be null
     */
    public HttpMethod httpMethod;

    public String httpBody;

    public Map<String, String> httpParams;

    /**
     * Header values
     */
    public Map<String, String> httpHeaders;

    private URLContext() {

    }

    public static URLContext build(HttpRequestVisitor visitor) {
        URLContext context = new URLContext();
        context.remoteAddress = visitor.accessRemoteAddress();
        context.httpMethod = visitor.accessHttpMethod();
        context.httpHeaders = visitor.accessHttpHeaders();
        context.httpParams = visitor.accessHttpParams();

        if (context.httpMethod != HttpMethod.GET)
            context.httpBody = visitor.accessHttpBody();

        return context;
    }
}
