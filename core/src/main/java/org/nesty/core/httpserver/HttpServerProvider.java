package org.nesty.core.httpserver;

/**
 * nesty
 *
 * HttpServer interface. global instance
 *
 * Author Michael on 03/03/2016.
 */
public abstract class HttpServerProvider implements HttpServer {

    private HttpServerOptions options = new HttpServerOptions();

    public HttpServerProvider useOptions(HttpServerOptions options) {
        this.options = options;
        return this;
    }

    public HttpServerOptions options() {
        return options;
    }
}
