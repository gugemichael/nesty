package org.nesty.core.httpserver.rest;

import org.nesty.commons.constant.http.HttpMethod;

import java.util.Map;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public interface HttpRequestVisitor {

    String visitRemoteAddress();

    HttpMethod visitHttpMethod();

    String visitHttpBody();

    Map<String, String> visitHttpParams();

    Map<String, String> visitHttpHeaders();
}
