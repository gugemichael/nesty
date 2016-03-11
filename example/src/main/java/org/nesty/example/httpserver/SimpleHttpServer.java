package org.nesty.example.httpserver;

import org.nesty.commons.exception.ControllerRequestMappingException;
import org.nesty.core.httpserver.HttpServer;
import org.nesty.core.httpserver.impl.async.AsyncHttpServerProvider;

public class SimpleHttpServer {

    public static void main(String[] args) throws ControllerRequestMappingException {

        // 1. create httpserver
        HttpServer server = AsyncHttpServerProvider.create("127.0.0.1", 8080);

        // 2. choose http params
        server.setMaxConnections(4096);
        server.setHandlerTimeout(10000);
        server.setIoThreads(8);
        server.setHandlerThreads(256);
        server.scanHttpController("com.nesty.test.neptune")
                .scanHttpController("org.nesty.example.httpserver.handler");

        // 3. start server and block for servicing
        if (!server.start()) {
            System.err.println("HttpServer run failed");
        }

        // would not to reach here ......
    }
}

