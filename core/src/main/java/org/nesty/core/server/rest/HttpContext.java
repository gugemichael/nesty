package org.nesty.core.server.rest;

import io.netty.handler.codec.http.HttpVersion;
import org.nesty.commons.constant.http.HttpConstants;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.core.server.rest.request.HttpRequestVisitor;

import java.util.Map;
import java.util.UUID;

/**
 * http context. include all http request and response information
 *
 * Author Michael on 03/03/2016.
 */
public class HttpContext extends HttpSession {

    // raw body
    protected String httpBody;
    // params include query string and body param
    protected Map<String, String> httpParams;
    // attribute map values
    protected Map<String, Object> httpAttributes;
    // header values
    protected Map<String, String> httpHeaders;
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
    // Http request method. NOT null
    private RequestMethod requestMethod;
    // Http long connection
    private boolean isKeepAlive = true;

    private HttpContext() {
    }

    public static HttpContext build(HttpRequestVisitor visitor) {
        HttpContext context = new HttpContext();
        context.remoteAddress = visitor.visitRemoteAddress();
        context.uri = visitor.visitURI();
        context.terms = visitor.visitTerms();
        context.requestMethod = visitor.visitHttpMethod();
        context.httpHeaders = visitor.visitHttpHeaders();
        context.httpParams = visitor.visitHttpParams();

        // TODO : if exclude GET or not ?
        //
        context.httpBody = visitor.visitHttpBody();

        if (visitor.visitHttpVersion() == HttpVersion.HTTP_1_1 &&
                HttpConstants.HEADER_CONNECTION_CLOSE.equals(context.httpHeaders.get(HttpConstants.HEADER_CONNECTION)))
            context.isKeepAlive = false;

        if (visitor.visitHttpVersion() == HttpVersion.HTTP_1_0 &&
                !HttpConstants.HEADER_CONNECTION_KEEPALIVE.equalsIgnoreCase(context.httpHeaders.get(HttpConstants.HEADER_CONNECTION)))
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

    public String getHttpBody() {
        return httpBody;
    }

    @Override
    public Map<String, String> getHttpParams() {
        return httpParams;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public Map<String, Object> getHttpAttributes() {
        return httpAttributes;
    }
}










