package org.nesty.core.httpserver.rest;

import org.nesty.commons.constant.http.RequestMethod;

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
     * whole http url exclude query string
     *
     */
    public String url;

    /**
     * http url terms split by "/"
     *
     */
    public String[] terms;

    /**
     * Http request method. can't be null
     */
    public RequestMethod requestMethod;

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
        context.remoteAddress = visitor.visitRemoteAddress();
        context.url = visitor.visitURL();
        context.terms = visitor.visitTerms();
        context.requestMethod = visitor.visitHttpMethod();
        context.httpHeaders = visitor.visitHttpHeaders();
        context.httpParams = visitor.visitHttpParams();

        if (context.requestMethod != RequestMethod.GET)
            context.httpBody = visitor.visitHttpBody();

        return context;
    }
}
