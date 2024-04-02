package com.samuel.registry;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryFactory {
    private static Map<String, Registry> registryCache = new ConcurrentHashMap<>();

    static {
        loadRegistry();
    }

    private static void loadRegistry() {
        ServiceLoader<Registry> load = ServiceLoader.load(Registry.class);
        Iterator<Registry> iterator = load.iterator();
        while (iterator.hasNext()) {
            Registry next = iterator.next();
            String clazzName = next.getClass().getSimpleName();

            registryCache.put(clazzName, next);
        }
    }

    private static Registry registry;
    public static Registry newInstance(String registryStr) {
        return registryCache.get(registryStr);
    }

}
