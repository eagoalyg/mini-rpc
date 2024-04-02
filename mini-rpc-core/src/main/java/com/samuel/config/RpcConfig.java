package com.samuel.config;

import cn.hutool.setting.dialect.Props;
import lombok.Data;

@Data
public class RpcConfig {
    private String address;

    private String port;

    private RegistryConfig registryConfig = new RegistryConfig();

}
