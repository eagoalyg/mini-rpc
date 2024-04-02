package com.samuel.proxy;

import java.lang.reflect.Proxy;

public class ServiceProxyFactory {

    public static<T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ServiceProxy());
    }
}
