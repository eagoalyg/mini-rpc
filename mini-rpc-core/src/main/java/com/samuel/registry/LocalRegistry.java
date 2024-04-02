package com.samuel.registry;

import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry {

    private static final ConcurrentHashMap<String, Class<?>> map = new ConcurrentHashMap<>();

    public static<T> void registry(String serviceName, Class<T> service) {
        map.put(serviceName, service);
    }

    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    public static void remove(String serviceName) {
        map.remove(serviceName);
    }
}
