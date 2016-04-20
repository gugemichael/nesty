package org.nesty.core.server.acceptor.http;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.nesty.core.server.NestyOptionProvider;
import org.nesty.core.server.NestyOptions;
import org.nesty.core.server.NestyServer;
import org.nesty.core.server.acceptor.AsyncAcceptor;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class AsyncHttpAcceptor extends AsyncAcceptor {

    public AsyncHttpAcceptor(NestyServer nestyServer, String address, int port) {
        super(nestyServer, address, port);
    }

    protected void buildPipeline(ChannelPipeline pipeline, NestyOptionProvider options) {
        pipeline.addLast("nesty-http-decoder", new HttpRequestDecoder())
                .addLast("nesty-http-aggregator", new HttpObjectAggregator(options.option(NestyOptions.MAX_PACKET_SIZE)))
                .addLast("nesty-request-handler", AsyncHttpHandler.build())
                .addLast("nesty-http-encoder", new HttpResponseEncoder());
    }
}
