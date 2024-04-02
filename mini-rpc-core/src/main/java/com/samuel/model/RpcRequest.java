package com.samuel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    /**
     * 类名
     * 方法名
     * 参数列表
     * 参数类型列表
     */
    private String serviceName;

    private String method;

    private Class<?>[] parameterType;

    private Object[] args;
}
