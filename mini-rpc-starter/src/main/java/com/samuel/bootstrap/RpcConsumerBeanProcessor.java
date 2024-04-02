package com.samuel.bootstrap;

import com.samuel.annotation.RpcClient;
import com.samuel.proxy.ServiceProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class RpcConsumerBeanProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();

        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            RpcClient rpcClient = field.getAnnotation(RpcClient.class);
            if (rpcClient == null) {
                continue;
            }

            Class<?> interfaceName = rpcClient.interfaceName();
            field.setAccessible(true);
            if (interfaceName == void.class) {
                interfaceName = field.getType();
            }
            Object proxy = ServiceProxyFactory.getProxy(interfaceName);
            try {
                field.set(bean, proxy);
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("注入Rpc代理对象异常");
            }
        }

        return bean;
    }
}
