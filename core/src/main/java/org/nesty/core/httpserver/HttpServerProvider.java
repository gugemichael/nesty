package org.nesty.core.httpserver;

/**
 * nesty
 *
 * HttpServer interface. global instance
 *
 * Author Michael on 03/03/2016.
 */
public abstract class HttpServerProvider implements HttpServer {

    /**
     * max connections in this httpserver instance
     *
     * incoming http connection over the number of maxConnections
     * will be reject and return http code 503
     */
    private int maxConnections = 10000;

    /**
     * max received packet size. default is 16MB
     */
    private int maxPacketSize = 16 * 1024 * 1024;

    /**
     * network socket io threads number. default is cpu core - 1
     *
     * DO NOT set the number more than cpu core number
     */
    private int ioThreads = Runtime.getRuntime().availableProcessors() - 1;

    /**
     * logic handler threads number. default is 128
     *
     * we suggest adjust the ioThreads number bigger if there are more
     * critical region code or block code in your handler logic (io intensive).
     * and smaller if your code has no block almost (cpu intensive)
     */
    private int handlerThreads = 128;

    /**
     * logic handler timeout. default is 30s
     */
    private int handleTimeout = 30000;


    @Override
    public HttpServer setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    @Override
    public HttpServer setIoThreads(int ioThreads) {
        this.ioThreads = ioThreads;
        return this;
    }

    @Override
    public HttpServer setHandlerThreads(int handlerThreads) {
        this.handlerThreads = handlerThreads;
        return this;
    }

    @Override
    public HttpServer setHandlerTimeout(int handleTimeout) {
        this.handleTimeout = handleTimeout;
        return this;
    }

    @Override
    public HttpServer setMaxPacketSize(int maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
        return null;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public int getIoThreads() {
        return ioThreads;
    }

    public int getHandlerThreads() {
        return handlerThreads;
    }

    public int getHandlerTimeout() {
        return handleTimeout;
    }

    public int getMaxPacketSize() {
        return maxPacketSize;
    }
}
