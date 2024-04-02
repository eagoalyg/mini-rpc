package com.samuel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceRegisterInfo {

    public String serviceName;

    public Class<?> aClass;
}
