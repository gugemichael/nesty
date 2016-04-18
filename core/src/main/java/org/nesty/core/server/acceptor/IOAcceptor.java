package org.nesty.core.server.acceptor;

public interface IOAcceptor {

    void eventLoop();

    void join() throws InterruptedException;

    void shutdown();
}
