package org.nesty.core.server.acceptor.spdy;

import io.netty.channel.ChannelPipeline;
import org.nesty.core.server.NestyOptionProvider;
import org.nesty.core.server.NestyServer;
import org.nesty.core.server.acceptor.AsyncAcceptor;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class AsyncSpdyAcceptor extends AsyncAcceptor {

    public AsyncSpdyAcceptor(NestyServer nestyServer, String address, int port) {
        super(nestyServer, address, port);
    }

    protected void buildPipeline(ChannelPipeline pipeline, NestyOptionProvider options) {
    }
}
