package com.samuel.bootstrap;

import com.samuel.SRpcApplication;
import com.samuel.model.ServiceMetaInfo;
import com.samuel.model.ServiceRegisterInfo;
import com.samuel.registry.LocalRegistry;
import com.samuel.registry.Registry;
import com.samuel.registry.RegistryFactory;
import com.samuel.server.HttpServer;
import com.samuel.server.VertxHttpServer;

import java.util.List;

public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo> serviceRegisterInfoList) {
        SRpcApplication.init();


        Registry registry = RegistryFactory.newInstance("zookeeper");
        for (ServiceRegisterInfo serviceRegisterInfo : serviceRegisterInfoList) {
            LocalRegistry.registry(serviceRegisterInfo.serviceName, serviceRegisterInfo.aClass);
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            //TODO get info from properties
            serviceMetaInfo.setAddress("");
            serviceMetaInfo.setPort(1);
            serviceMetaInfo.setServiceName(serviceRegisterInfo.getServiceName());
            registry.register(serviceMetaInfo);
        }

        HttpServer server = new VertxHttpServer();
        //TODO
        server.doStart(8000);
    }
}
