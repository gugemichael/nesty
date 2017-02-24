package org.nesty.example.httpserver;

import org.nesty.commons.exception.ControllerRequestMappingException;
import org.nesty.core.server.NestyOptions;
import org.nesty.core.server.NestyServer;
import org.nesty.core.server.acceptor.AsyncServerProvider;
import org.nesty.core.server.protocol.NestyProtocol;

public class SimpleHttpServer {

    public static void main(String[] args) throws ControllerRequestMappingException, InterruptedException {
        startServer(args, false);
    }

    public static void startServer(String[] args, boolean withSpringController) throws ControllerRequestMappingException, InterruptedException {
        // 1. create httpserver
        final NestyServer server = AsyncServerProvider.builder().address("127.0.0.1").port(8080)
                .service(NestyProtocol.HTTP);

        // 2. choose http params. this is unnecessary
        server.option(NestyOptions.IO_THREADS, Runtime.getRuntime().availableProcessors())
                .option(NestyOptions.WORKER_THREADS, 128)
                .option(NestyOptions.TCP_BACKLOG, 1024)
                .option(NestyOptions.TCP_NODELAY, true)
                .option(NestyOptions.ACCESS_LOG, "/tmp/accesslog");

        // 3. scan defined controller class with package name
        server.scanHttpController("com.nesty.test.neptune")
                .scanHttpController("com.nesty.test.billing")
                .scanHttpController("org.nesty.example.httpserver.handler");

        if (withSpringController) {
            server.scanHttpController("org.nesty.example.httpserver.spring");
        }

        // 4. start http server
        if (!server.start())
            System.err.println("NestServer run failed");

        server.join();

        // would not to reach here as usual ......
        server.shutdown();
    }
}

