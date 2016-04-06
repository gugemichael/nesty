package org.nesty.core.httpserver.utils;

import java.util.concurrent.atomic.AtomicLong;

public class Countable {

    private final AtomicLong counter = new AtomicLong();

    public long hit() {
        return counter.incrementAndGet();
    }

    public long count() {
        return counter.get();
    }
}
