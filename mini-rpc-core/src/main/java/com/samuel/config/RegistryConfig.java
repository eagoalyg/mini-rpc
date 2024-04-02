package com.samuel.config;

import lombok.Data;

@Data
public class RegistryConfig {
    private String registry = "RedisRegistry";

    private String address = "127.0.0.1";

    private Integer port = 6379;

    private String username;

    private String password;

    private Long timeout = 100000L;
}
