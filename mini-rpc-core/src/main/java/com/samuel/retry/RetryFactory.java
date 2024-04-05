package com.samuel.retry;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class RetryFactory {

    private static Map<String, Retry> retryCache = new ConcurrentHashMap<>();

    static {
        Iterator<Retry> iterator = ServiceLoader.load(Retry.class).iterator();
        while (iterator.hasNext()) {
            Retry next = iterator.next();
            retryCache.put(next.getClass().getSimpleName(), next);
        }
    }

    public static Retry getInstance(String name) {
        return retryCache.get(name);
    }
}
