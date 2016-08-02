package org.nesty.core.server.rest.controller;

import org.nesty.commons.annotations.Controller;
import org.nesty.commons.annotations.RequestMapping;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.core.server.NestyServerMonitor;
import org.nesty.core.server.rest.URLResource;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DefaultController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RootResponse root() {
        RootResponse response = new RootResponse();
        response.REQUESTS_MISS = NestyServerMonitor.getRequestMiss();
        response.REQUEST_HITS = NestyServerMonitor.getRequestHit();
        response.CONNECTIONS = NestyServerMonitor.getConnections();
        response.LAST_SERV_TIME = NestyServerMonitor.getLastServTime();
        response.LAST_SERV_ID = NestyServerMonitor.getLastServID();
        response.LAST_SERV_FAIL_ID = NestyServerMonitor.getLastServFailID();

        for (Map.Entry<URLResource, URLController> entry : NestyServerMonitor.getResourcesMap().entrySet())
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
