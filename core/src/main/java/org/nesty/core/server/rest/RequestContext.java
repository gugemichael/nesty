package org.nesty.core.server.rest;

import org.nesty.commons.constant.http.HttpConstants;
import org.nesty.commons.constant.RequestMethod;
import org.nesty.core.server.protocol.NestyProtocol;
import org.nesty.core.server.rest.request.RequestVisitor;

import java.util.Map;
import java.util.UUID;

/**
 * http context. include all http request and response information
 * <p>
 * Author Michael on 03/03/2016.
 */
public class RequestContext extends Session {

    // raw body
    private String body;
    // params include query string and body param
    private Map<String, String> params;
    // attribute map values
    private Map<String, Object> attributes;
    // header values
    private Map<String, String> headers;
    // client request with unique id, fetch it from http header(Request-Id)
    // if exsit. or generate by UUID
    private String requestId = UUID.randomUUID().toString();
    // ip address from origin client, fetch it from getRemoteAddr()
    // or header X-FORWARDED-FOR
    private String remoteAddress;
    //  raw uri exclude query string
    private String uri;
    // http uri terms split by "/"
    private String[] terms;
    // protocol http. http_1_1. http_2. spdy
    private NestyProtocol protocol;
    // Http request method. NOT null
    private RequestMethod requestMethod;
    // Http long connection
    private boolean isKeepAlive = true;

    private RequestContext() {
    }

    public static RequestContext build(RequestVisitor visitor) {
        RequestContext context = new RequestContext();
        context.remoteAddress = visitor.remoteAddress();
        context.uri = visitor.uri();
        context.terms = visitor.terms();
        context.requestMethod = visitor.method();
        context.headers = visitor.headers();
        context.params = visitor.params();

        // TODO : if exclude GET or not ?
        //
        context.body = visitor.body();

        context.protocol = visitor.protocol();

        if (context.protocol == NestyProtocol.HTTP &&
                HttpConstants.HEADER_CONNECTION_CLOSE.equals(context.headers.get(HttpConstants.HEADER_CONNECTION)))
            context.isKeepAlive = false;

        if (context.protocol == NestyProtocol.HTTP_1_0 &&
                !HttpConstants.HEADER_CONNECTION_KEEPALIVE.equalsIgnoreCase(context.headers.get(HttpConstants.HEADER_CONNECTION)))
            context.isKeepAlive = false;

        return context;
    }

    public boolean isKeepAlive() {
        return isKeepAlive;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getUri() {
        return uri;
    }

    public String[] getTerms() {
        return terms;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public NestyProtocol getProtocol() {
        return protocol;
    }
}










