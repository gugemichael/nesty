package org.nesty.core.httpserver;

import org.nesty.core.httpserver.rest.URLResource;
import org.nesty.core.httpserver.rest.controller.URLController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class HttpServerStats {

    public static final AtomicLong REQUESTS_MISS = new AtomicLong();
    public static final AtomicLong REQUESTS_HIT = new AtomicLong();
    public static final AtomicLong CONNECTIONS = new AtomicLong();

    public static Map<URLResource, URLController> RESOURCES = new HashMap<>();

    public static volatile long LAST_SERV_TIME = System.currentTimeMillis();

    public static volatile String LAST_SERV_ID;
    public static volatile String LAST_SERV_FAIL_ID;
}
