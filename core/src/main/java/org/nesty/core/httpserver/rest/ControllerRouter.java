package org.nesty.core.httpserver.rest;

import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.core.httpserver.rest.controller.URLController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Routeable Controller mapping
 *
 * Author : Michael
 * Date : March 07, 2016
 */
public class ControllerRouter {

    /**
     * first index layer. indexd by HttpMethod and URL terms length
     *
     * our max supportted URL terms is 512
     */
    private final URLMapper[][] mapper = new URLMapper[RequestMethod.UNKOWN.ordinal()][512];

    /**
     * get specified resource's handler or null if it don't exist
     *
     * @param resource url anaylzed resource
     * @return handler instance or null if it don't exist
     */
    public URLController findURLControlloer(URLResource resource) {
        try {
            // we have checked the request method is supportted!. couldn't index overflow
            URLMapper slot = mapper[resource.getRequestMethod().ordinal()][resource.getFragments().size()];
            if (slot != null)
                return slot.get(resource);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    public synchronized boolean register(URLResource resource, URLController handler) {
        int httpMethodIndex = resource.getRequestMethod().ordinal();
        int termsIndex = resource.getFragments().size();
        if (mapper[httpMethodIndex][termsIndex] == null)
            mapper[httpMethodIndex][termsIndex] = new URLMapper();
        return mapper[httpMethodIndex][termsIndex].register(resource, handler);
    }

    class URLMapper {
        private final Map<URLResource, URLController> controller = new ConcurrentHashMap<URLResource, URLController>(256);

        public URLController get(URLResource resource) {
            return controller.get(resource);
        }

        public boolean register(URLResource resource, URLController handler) {
            return controller.put(resource, handler) == null;
        }
    }
}
