package org.nesty.core.httpserver.rest.route;

import org.nesty.core.httpserver.rest.handler.URLHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Routeable Controller mapping
 *
 * Author : Michael
 * Date : March 07, 2016
 */
public class RouteControlloer {

    /**
     * Restful mapping
     */
    private final ConcurrentReadRouteMap controller;

    public RouteControlloer(ConcurrentReadRouteMap controllerMap) {
        this.controller = controllerMap;
    }

    /**
     * get specified resource's handler or null if it don't exist
     *
     * @param resource url anaylzed resource
     * @return handler instance or null if it don't exist
     */
    public URLHandler findURLHandler(URLResource resource) {
        return controller.get(resource);
    }

    public static class ConcurrentReadRouteMap {
        private Map<URLResource, URLHandler> map = new HashMap<>(128);

        public synchronized void put(URLResource resource, URLHandler handler) {
            this.map.put(resource, handler);
        }

        public URLHandler get(URLResource resource) {
            return this.map.get(resource);
        }

    }
}
