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
public class AsyncHttpsAcceptor extends AsyncAcceptor {

    public AsyncHttpsAcceptor(NestyServer nestyServer, String address, int port) {
        super(nestyServer, address, port);
    }

    protected void buildPipeline(ChannelPipeline pipeline, NestyOptionProvider options) {
        throw new RuntimeException("not implement");
    }
}
