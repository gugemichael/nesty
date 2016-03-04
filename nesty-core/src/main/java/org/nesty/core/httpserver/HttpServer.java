package org.nesty.core.httpserver;

import org.nesty.commons.exception.NestyControllerScanException;

/**
 * nesty
 *
 * HttpServer interface. global instance
 *
 * Author Michael on 03/03/2016.
 */
public interface HttpServer {

    /**
     * Startup the server. this method will blocked
     * util the server shutdown. or return false on error
     */
    Boolean start();

    /**
     * Configure http max packet length limit
     *
     * @param maxPacketSize max http packet size
     */
    HttpServer setMaxPacketSize(int maxPacketSize);

    /**
     * Configure max client connections at the same time. and
     * HttpServer will return http code 503
     *
     * @param maxConnections max connections number
     */
    HttpServer setMaxConnections(int maxConnections);

    /**
     * Configure client connection's max handle timeout. after this
     * time HttpServer will close the connection with http code 504.
     *
     * @param handleTimeout max connections number
     */
    HttpServer setHandlerTimeout(int handleTimeout);


    /**
     * Configure network socket io threads pool size
     *
     * @param ioThreads max io threads number
     */
    HttpServer setIoThreads(int ioThreads);

    /**
     * Configure user logic threads pool size
     *
     * @param handlerThreads max user logic threads number
     */
    HttpServer setHandlerThreads(int handlerThreads);

    /**
     * Scan the the package that contains all of handlers to be
     * register
     *
     * @param packageName package path name in string format
     */
    HttpServer scanHttpController(String packageName) throws NestyControllerScanException;
}
