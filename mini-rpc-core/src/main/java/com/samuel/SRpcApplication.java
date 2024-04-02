package com.samuel;

import cn.hutool.setting.dialect.Props;
import com.samuel.config.RegistryConfig;
import com.samuel.config.RpcConfig;
import com.samuel.model.ServiceMetaInfo;
import com.samuel.registry.Registry;
import com.samuel.registry.RegistryFactory;
import com.samuel.util.AddressUtil;

public class SRpcApplication {

    public static void init() {
        // load registry config
        //TODO getRegistryConfig()
        RpcConfig rpcConfig = loadRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.newInstance(registryConfig.getRegistry());
        registry.init(registryConfig);

        //TODO 未调用
        Runtime.getRuntime().addShutdownHook(new Thread(registry::shutdown));
    }

    private static RpcConfig loadRpcConfig() {
        Props props = new Props("application.properties");
        return props.toBean(RpcConfig.class);
    }

}
