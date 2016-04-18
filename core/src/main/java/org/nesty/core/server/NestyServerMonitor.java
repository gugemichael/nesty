package org.nesty.core.server;

import org.nesty.core.server.rest.URLResource;
import org.nesty.core.server.rest.controller.URLController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class NestyServerMonitor {

    // total missed request times
    static final AtomicLong requestMiss = new AtomicLong();
    // total hit request times
    static final AtomicLong requestHit = new AtomicLong();
    // total connections on current
    static final AtomicLong connections = new AtomicLong();
    // all URLResource mapping for hit count
    static Map<URLResource, URLController> resourceMap = new HashMap<>();
    // last regular service timestamp
    static volatile long lastServTime = System.currentTimeMillis();
    // last regular request id
    static volatile String lastServID;
    // last regular failed request id
    static volatile String lastServFailID;

    // stats switch
    private static boolean inUse = true;

    public static long getRequestMiss() {
        return requestMiss.longValue();
    }

    public static void incrRequestMiss() {
        if (inUse)
            requestMiss.incrementAndGet();
    }

    public static long getRequestHit() {
        return requestHit.longValue();
    }

    public static void incrRequestHit() {
        if (inUse)
            requestHit.incrementAndGet();
    }

    public static long getConnections() {
        return connections.longValue();
    }

    public static void incrConnections() {
        if (inUse)
            connections.incrementAndGet();
    }

    public static void decrConnections() {
        if (inUse)
            connections.decrementAndGet();
    }

    public static long getLastServTime() {
        return lastServTime;
    }

    public static void setLastServTime(long lastServTime) {
        if (inUse)
            NestyServerMonitor.lastServTime = lastServTime;
    }

    public static String getLastServID() {
        return lastServID;
    }

    public static void setLastServID(String lastServID) {
        if (inUse)
            NestyServerMonitor.lastServID = lastServID;
    }

    public static Map<URLResource, URLController> getResourcesMap() {
        return resourceMap;
    }

    public static void setResourceMap(Map<URLResource, URLController> resourceMap) {
        if (inUse)
            NestyServerMonitor.resourceMap = resourceMap;
    }

    public static String getLastServFailID() {
        return lastServFailID;
    }

    public static void setLastServFailID(String lastServFailID) {
        if (inUse)
            NestyServerMonitor.lastServFailID = lastServFailID;
    }

    public static void disable() {
        inUse = false;
    }

    public static void enable() {
        inUse = true;
    }
}
