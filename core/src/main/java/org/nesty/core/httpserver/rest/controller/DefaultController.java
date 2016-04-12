package org.nesty.core.httpserver.rest.controller;

import org.nesty.commons.annotations.Controller;
import org.nesty.commons.annotations.RequestMapping;
import org.nesty.core.httpserver.HttpServerStats;
import org.nesty.core.httpserver.rest.URLResource;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DefaultController {

    @RequestMapping("/")
    public RootResponse root() {
        RootResponse response = new RootResponse();
        response.REQUESTS_MISS = HttpServerStats.getRequestMiss();
        response.REQUEST_HITS = HttpServerStats.getRequestHit();
        response.CONNECTIONS = HttpServerStats.getConnections();
        response.LAST_SERV_TIME = HttpServerStats.getLastServTime();
        response.LAST_SERV_ID = HttpServerStats.getLastServID();
        response.LAST_SERV_FAIL_ID = HttpServerStats.getLastServFailID();

        for (Map.Entry<URLResource, URLController> entry : HttpServerStats.getResourcesMap().entrySet())
            response.RESOURCES_HITS.put(entry.getKey().toString(), entry.getValue().count());

        return response;
    }

    public static class RootResponse {
        public long REQUESTS_MISS;
        public long REQUEST_HITS;
        public long CONNECTIONS;
        public long LAST_SERV_TIME = System.currentTimeMillis();
        public String LAST_SERV_ID;
        public String LAST_SERV_FAIL_ID;
        public Map<String, Long> RESOURCES_HITS = new HashMap<>();
    }
}
