package org.nesty.core.server.rest;

import org.nesty.commons.constant.RequestMethod;
import org.nesty.core.server.rest.controller.URLController;

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
     * get specified resource's controller or null if it don't exist
     *
     * @param resource url anaylzed resource
     * @return controller instance or null if it don't exist
     */
    public URLController findURLController(URLResource resource) {
        try {
            // we have checked the request method is supportted!. couldn't index overflow
            URLMapper slot = mapper[resource.requestMethod().ordinal()][resource.fragments().size()];
            if (slot != null)
                return slot.get(resource);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    public synchronized boolean register(URLResource resource, URLController controller) {
        Indexer indexer = new Indexer(resource);
        if (mapper[indexer.MethodIndex][indexer.TermsIndex] == null)
            mapper[indexer.MethodIndex][indexer.TermsIndex] = new URLMapper();
        return mapper[indexer.MethodIndex][indexer.TermsIndex].register(resource, controller);
    }

    public synchronized void unregister(URLResource resource) {
        Indexer indexer = new Indexer(resource);
        if (mapper[indexer.MethodIndex][indexer.TermsIndex] != null)
            mapper[indexer.MethodIndex][indexer.TermsIndex].unregister(resource);
    }

    static class Indexer {
        public int MethodIndex;
        public int TermsIndex;

        public Indexer(URLResource resource) {
            MethodIndex = resource.requestMethod().ordinal();
            TermsIndex = resource.fragments().size();
        }
    }

    static class URLMapper {
        private final Map<URLResource, URLController> controller = new ConcurrentHashMap<URLResource, URLController>(256);

        public URLController get(URLResource resource) {
            return controller.get(resource);
        }

        public boolean register(URLResource resource, URLController controller) {
            return this.controller.put(resource, controller) == null;
        }

        public void unregister(URLResource resource) {
            controller.remove(resource);
        }
    }
}
