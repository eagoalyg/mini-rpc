package com.samuel.registry;

import com.samuel.config.RegistryConfig;
import com.samuel.model.ServiceMetaInfo;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

public class ZookeeperRegistry implements Registry {
    private CuratorFramework client;
    @Override
    public void shutdown() {
        client.close();
    }

    @Override
    public void init(RegistryConfig registryConfig) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("127.0.0.1:", retryPolicy);
        client.start();
    }

    @Override
    public List<ServiceMetaInfo> discoverService(String serviceName) {
        return null;
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {

    }
}
