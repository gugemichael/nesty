package org.nesty.core.server.acceptor;

import org.nesty.core.server.NestyServer;
import org.nesty.core.server.acceptor.http.AsyncHttpAcceptor;
import org.nesty.core.server.acceptor.spdy.AsyncSpdyAcceptor;
import org.nesty.core.server.protocol.NestyHttpServer;
import org.nesty.core.server.protocol.NestyProtocol;
import org.nesty.core.server.protocol.NestySpdyServer;

/**
 * Async http server in netty eventloop
 * <p>
 * Author Michael on 03/03/2016.
 */
public class AsyncServerProvider {

    private AsyncServerProvider() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        // local address
        String address = "0.0.0.0";
        // local port
        int port = 8080;

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public NestyServer service(NestyProtocol protocol) {
            switch (protocol) {
            case HTTP_1_0:
            case HTTP:
                NestyHttpServer http = new NestyHttpServer();
                http.ioAcceptor(new AsyncHttpAcceptor(http, address, port));
                return http;
            case SPDY:
                NestySpdyServer spdy = new NestySpdyServer();
                spdy.ioAcceptor(new AsyncSpdyAcceptor(spdy, address, port));
                return spdy;
            case HTTPS:
            case HTTP2:
            default:
                throw new IllegalArgumentException(String.format("protocol %s is not support now !!", protocol));
            }
        }
    }
}
