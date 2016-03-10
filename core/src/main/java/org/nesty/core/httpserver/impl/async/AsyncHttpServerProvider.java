package org.nesty.core.httpserver.impl.async;

import org.nesty.core.httpserver.HttpServer;
import org.nesty.core.httpserver.ScanableHttpServerProvider;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class AsyncHttpServerProvider extends ScanableHttpServerProvider {

    /**
     * http protocol acceptor
     */
    private final IOAcceptor ioAcceptor;

    public AsyncHttpServerProvider(String address, int port) {
        this.ioAcceptor = new IOAcceptor(this, address, port);
    }


    public static HttpServer create(String address, Integer port) {
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

}
