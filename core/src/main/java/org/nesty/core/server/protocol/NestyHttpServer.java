package org.nesty.core.server.protocol;

import org.nesty.core.server.NestyServer;
import org.nesty.core.server.acceptor.IOAcceptor;

public class NestyHttpServer extends NestyServer {

    /**
     * core io acceptor. implement http protocol
     */
    private IOAcceptor ioAcceptor;

    public NestyHttpServer ioAcceptor(IOAcceptor asyncAcceptor) {
        this.ioAcceptor = asyncAcceptor;
        return this;
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
