package org.nesty.core.server.rest.request;

import org.nesty.commons.constant.RequestMethod;
import org.nesty.core.server.protocol.NestyProtocol;

import java.util.Map;

/**
 * nesty
 * <p>
 * Author Michael on 03/03/2016.
 */
public interface RequestVisitor {
    String remoteAddress();

    String uri();

    String[] terms();

    RequestMethod method();

    String body();

    Map<String, String> params();

    Map<String, String> headers();

    NestyProtocol protocol();
}
