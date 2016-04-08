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
        response.REQUESTS_MISS = HttpServerStats.REQUESTS_MISS.get();
        response.REQUEST_HITS = HttpServerStats.REQUESTS_HIT.get();
        response.CONNECTIONS = HttpServerStats.CONNECTIONS.get();
        response.LAST_SERV_TIME = HttpServerStats.LAST_SERV_TIME;
        response.LAST_SERV_ID = HttpServerStats.LAST_SERV_ID;
        response.LAST_SERV_FAIL_ID = HttpServerStats.LAST_SERV_FAIL_ID;

        for (Map.Entry<URLResource, URLController> entry : HttpServerStats.RESOURCES.entrySet())
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
