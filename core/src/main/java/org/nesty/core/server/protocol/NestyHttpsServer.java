package org.nesty.core.server.protocol;

import org.nesty.core.server.acceptor.IOAcceptor;
import org.nesty.core.server.NestyServer;

public class NestyHttpsServer extends NestyServer {

    /**
     * core io acceptor. implement http protocol
     */
    private IOAcceptor ioAcceptor;

    public NestyHttpsServer ioAcceptor(IOAcceptor asyncAcceptor) {
        this.ioAcceptor = asyncAcceptor;
        return this;
    }

    @Override
    public Boolean start() {
        return Boolean.FALSE;
    }

    @Override
    public void join() throws InterruptedException {
    }

    @Override
    public void shutdown() {
    }
}
