package com.samuel.bootstrap;

import com.samuel.annotation.RpcService;
import com.samuel.model.ServiceMetaInfo;
import com.samuel.registry.LocalRegistry;
import com.samuel.registry.Registry;
import com.samuel.registry.RegistryFactory;
import com.samuel.util.AddressUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class RpcProviderBeanProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class<?> aClass = o.getClass();
        RpcService rpcService = aClass.getAnnotation(RpcService.class);
        if (rpcService == null) {
            return o;
        }

        // 注册到本地池
        Class<?> interfaceClass = rpcService.interfaceClass();
        LocalRegistry.registry(interfaceClass.getName(), aClass);

        // 注册到注册中心
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setAddress(AddressUtil.getLocalHost());
        serviceMetaInfo.setPort(8888);
        serviceMetaInfo.setServiceName(interfaceClass.getName());
        Registry registry = RegistryFactory.newInstance("RedisRegistry");
        //TODO[optimistic] 每个类都需要开启一个定时任务去刷新
        registry.register(serviceMetaInfo);
        return o;
    }
}
