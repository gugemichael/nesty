package org.nesty.example.httpserver;

import org.nesty.commons.exception.ControllerRequestMappingException;
import org.nesty.core.httpserver.HttpServerOptions;
import org.nesty.core.httpserver.HttpServerRouteProvider;
import org.nesty.core.httpserver.impl.async.AsyncHttpServerProvider;

public class SimpleHttpServer {

    public static void main(String[] args) throws ControllerRequestMappingException {

        // 1. create httpserver
        HttpServerRouteProvider server = AsyncHttpServerProvider.create("127.0.0.1", 8080);

        // 2. choose http params. this is unnecessary
        server.useOptions(new HttpServerOptions().setMaxConnections(4096)
                                                .setHandlerTimeout(10000)
                                                .setIoThreads(8)
                                                .setHandlerThreads(256));

        server.scanHttpController("com.nesty.test.neptune")
                .scanHttpController("com.nesty.test.billing")
                .scanHttpController("org.nesty.example.httpserver.handler");

        // 3. start http server
        if (!server.start())
            System.err.println("HttpServer run failed");

        try {
            // join and wait here
            server.join();
            server.shutdown();
        } catch (InterruptedException ignored) {
        }

        // would not to reach here as usual ......
    }
}

