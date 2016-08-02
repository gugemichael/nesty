package org.nesty.core.server.acceptor.https;

import io.netty.channel.ChannelPipeline;
import org.nesty.core.server.NestyOptionProvider;
import org.nesty.core.server.NestyServer;
import org.nesty.core.server.acceptor.AsyncAcceptor;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class HttpsAsyncAcceptor extends AsyncAcceptor {

    public HttpsAsyncAcceptor(NestyServer nestyServer, String address, int port) {
        super(nestyServer, address, port);
    }

    protected void protocolPipeline(ChannelPipeline pipeline, NestyOptionProvider options) {
        throw new RuntimeException("not implement");
    }
}
