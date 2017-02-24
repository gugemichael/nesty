package org.nesty.example.httpserver.spring;

import org.nesty.example.httpserver.SimpleHttpServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by adol on 17-2-24.
 */
public class Env {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext();

        ctx.register(EnvConfig.class);
        ctx.refresh();

        SimpleHttpServer.startServer(args, true);
    }
}
