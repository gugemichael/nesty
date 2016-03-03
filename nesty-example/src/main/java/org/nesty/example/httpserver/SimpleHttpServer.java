package org.nesty.example.httpserver;


import org.nesty.core.httpserver.HttpServer;
import org.nesty.core.httpserver.impl.async.AsyncHttpServerProvider;

/**
 * nesty-example
 *
 * Author Michael on 03/03/2016
 */
public class SimpleHttpServer {
    public static void main(String[] args) {

        // 1. create httpserver
        HttpServer server = AsyncHttpServerProvider.create("127.0.0.1", 8080);

        // 2. choose http params
        server.setMaxConnections(128);
        server.setIoThreads(4);
        server.setHandlerThreads(128);
        server.scanHttpProvider("org.nesty.example.httpserver");

        // 3. start server
        if (!server.start()) {
            System.err.println("HttpServer run failed");
        }

        System.out.println("DONE");
    }
}
