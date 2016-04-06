package org.nesty.core.httpserver.rest.route;

import org.nesty.core.httpserver.rest.URLHandler;
import org.nesty.core.httpserver.rest.URLResource;

import java.util.HashMap;
import java.util.Map;

/**
 * Routeable Controller mapping
 *
 * Author : Michael
 * Date : March 07, 2016
 */
public class RouteControlloer {

    private final Map<URLResource, URLHandler> controller = new HashMap<>(256);

    public RouteControlloer() {
    }

    /**
     * get specified resource's handler or null if it don't exist
     *
     * @param resource url anaylzed resource
     * @return handler instance or null if it don't exist
     */
    public URLHandler findURLControlloer(URLResource resource) {
        return controller.get(resource);
    }

    public synchronized boolean put(URLResource resource, URLHandler handler) {
        return controller.put(resource, handler) == null;
    }
}
