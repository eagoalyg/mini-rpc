package com.samuel.registry;

import com.samuel.config.RegistryConfig;
import com.samuel.model.ServiceMetaInfo;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface Registry {

    //TODO disconnect remote registry
    void shutdown();

    //TODO connect to remote registry
    void init(RegistryConfig registryConfig);

    //TODO query service info from zookeeper
    List<ServiceMetaInfo> discoverService(String serviceName);

    void register(ServiceMetaInfo serviceMetaInfo);
}
