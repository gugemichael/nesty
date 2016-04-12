package org.nesty.core.httpserver.impl.async;

import org.nesty.core.httpserver.HttpServerRouteProvider;

/**
 * Async http server in netty eventloop
 *
 * Author Michael on 03/03/2016.
 */
public class AsyncHttpServerProvider extends HttpServerRouteProvider {

    // http protocol acceptor
    private final IOAcceptor ioAcceptor;

    private AsyncHttpServerProvider(String address, int port) {
        this.ioAcceptor = new IOAcceptor(this, address, port);
    }

    public static AsyncHttpServerProvider create(String address, Integer port) {
        return new AsyncHttpServerProvider(address, port);
    }

    @Override
    public Boolean start() {

        try {

            // run event loop and joined
            ioAcceptor.eventLoop();

            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    @Override
    public void join() throws InterruptedException {
        ioAcceptor.join();
    }

    @Override
    public void shutdown() {
        ioAcceptor.shutdown();
    }
}
