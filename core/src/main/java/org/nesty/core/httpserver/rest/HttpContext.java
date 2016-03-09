package org.nesty.core.httpserver.rest;

import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.core.httpserver.rest.request.HttpRequestVisitor;

import java.util.Map;
import java.util.UUID;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class HttpContext {

    /**
     * client request with unique id, fetch it from http header(Request-Id)
     * if exsit. or generate by UUID
     */
    private String requestId = UUID.randomUUID().toString();

    /**
     * ip address from origin client, fetch it from getRemoteAddr()
     * or header X-FORWARDED-FOR
     */
    private String remoteAddress;

    /**
     * whole http url exclude query string
     *
     */
    private String url;

    /**
     * http url terms split by "/"
     *
     */
    private String[] terms;

    /**
     * Http request method. can't be null
     */
    private RequestMethod requestMethod;

    private String httpBody;

    private Map<String, String> httpParams;

    /**
     * Header values
     */
    private Map<String, String> httpHeaders;

    public String getRequestId() {
        return requestId;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getUrl() {
        return url;
    }

    public String[] getTerms() {
        return terms;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getHttpBody() {
        return httpBody;
    }

    public Map<String, String> getHttpParams() {
        return httpParams;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    private HttpContext() {

    }

    public static HttpContext build(HttpRequestVisitor visitor) {
        HttpContext context = new HttpContext();
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
