package org.nesty.core.server.rest.request;

import io.netty.handler.codec.http.HttpVersion;
import org.nesty.commons.constant.http.RequestMethod;

import java.util.Map;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public interface HttpRequestVisitor {
    String visitRemoteAddress();

    String visitURI();

    String[] visitTerms();

    RequestMethod visitHttpMethod();

    String visitHttpBody();

    Map<String, String> visitHttpParams();

    Map<String, String> visitHttpHeaders();

    HttpVersion visitHttpVersion();
}
