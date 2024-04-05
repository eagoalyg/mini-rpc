package com.samuel.server;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.body.RequestBody;
import com.samuel.model.RpcRequest;
import com.samuel.model.RpcResponse;
import com.samuel.model.ServiceMetaInfo;
import com.samuel.serializer.Serializer;

import java.io.IOException;
import java.io.Serializable;

public class HttpClient {

    public static RpcResponse doRequest(Serializer serializer, RpcRequest requestBody, ServiceMetaInfo serviceMetaInfo) throws IOException {
        byte[] bytes = serializer.serialize(requestBody);
        try (HttpResponse response = HttpRequest.post(serviceMetaInfo.getFullyAddress()).body(bytes).execute()) {
            byte[] bodyBytes = response.bodyBytes();
            return serializer.deserialize(bodyBytes, RpcResponse.class);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("异常");
        }
    }
}
