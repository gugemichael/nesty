package org.nesty.core.httpserver.rest.request;

import org.nesty.commons.constant.http.RequestMethod;

import java.util.Map;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public interface HttpRequestVisitor {
    String visitRemoteAddress();
    String visitURL();
    String[] visitTerms();
    RequestMethod visitHttpMethod();
    String visitHttpBody();
    Map<String, String> visitHttpParams();
    Map<String, String> visitHttpHeaders();
}
