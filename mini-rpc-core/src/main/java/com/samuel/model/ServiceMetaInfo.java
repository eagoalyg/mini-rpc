package com.samuel.model;

import lombok.Data;

@Data
public class ServiceMetaInfo {
    private String address;

    private Integer port;

    /**
     * 需要调用的全限定类名
     */
    private String serviceName;

    /**
     * format 127.0.0.1:8080
     * @return
     */
    public String getFullyAddress() {
        return address + ":" + port;
    }
}
