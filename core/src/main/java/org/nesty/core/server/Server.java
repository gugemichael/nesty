package org.nesty.core.server;

/**
 * NestServer interface. global instance
 *
 * Author Michael on 03/03/2016.
 */
public interface Server {

    /**
     * startup the http service.
     *
     * @return true if listen service estanbulished or false on error
     */
    Boolean start();

    /**
     * keep servicing and block to waitting shutdown
     */
    void join() throws InterruptedException;

    /**
     * destroy the service
     */
    void shutdown();
}
