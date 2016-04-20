package org.nesty.core.server.acceptor.spdy;

import io.netty.channel.Channel;
import org.nesty.commons.constant.RequestMethod;
import org.nesty.core.server.protocol.NestyProtocol;
import org.nesty.core.server.rest.request.RequestVisitor;

import java.util.Map;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class SpdyRequestVisitor implements RequestVisitor {

    private final Channel channel;

    public SpdyRequestVisitor(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String remoteAddress() {
        return null;
    }

    @Override
    public RequestMethod method() {
        return null;
    }

    @Override
    public String body() {
        return null;
    }

    @Override
    public Map<String, String> params() {
        return null;
    }

    @Override
    public Map<String, String> headers() {
        return null;
    }

    @Override
    public String uri() {
        return null;
    }

    @Override
    public String[] terms() {
        return null;
    }

    @Override
    public NestyProtocol protocol() {
        return NestyProtocol.SPDY;
    }
}
