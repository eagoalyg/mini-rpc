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
public class RpcResponse implements Serializable {
    /**
     * 类名
     * 返回数据
     * 返回信息
     * 异常信息
     */
    private Object data;

    private Class<?> dataType;

    private String message;

    private String exception;
}
