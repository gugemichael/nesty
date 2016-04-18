package org.nesty.core.server.acceptor;

import org.nesty.core.server.NestyServer;
import org.nesty.core.server.acceptor.http.HttpAsyncAcceptor;
import org.nesty.core.server.protocol.NestyHttpServer;
import org.nesty.core.server.protocol.NestyProtocol;

/**
 * Async http server in netty eventloop
 *
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
            case HTTP:
                NestyHttpServer httpServer = new NestyHttpServer();
                httpServer.ioAcceptor(new HttpAsyncAcceptor(httpServer, address, port));
                return httpServer;
            case HTTPS:
            case SPDY:
            case HTTP2:
            default:
                throw new IllegalArgumentException(String.format("protocol %s is not support now !!", protocol));
            }
        }
    }
}
