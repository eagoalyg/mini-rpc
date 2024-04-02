package com.samuel.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.samuel.annotation.RpcClient;
import com.samuel.model.RpcRequest;
import com.samuel.model.RpcResponse;
import com.samuel.model.ServiceMetaInfo;
import com.samuel.registry.Registry;
import com.samuel.registry.RegistryFactory;
import com.samuel.serializer.Serializer;
import com.samuel.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest requestBody = RpcRequest.builder()
                .serviceName(serviceName)
                .method(method.getName())
                .parameterType(method.getParameterTypes())
                .args(args)
                .build();

        //TODO query from config
        Registry registry = RegistryFactory.newInstance("RedisRegistry");
        List<ServiceMetaInfo> serviceMetaInfos = registry.discoverService(serviceName);
        if (serviceMetaInfos.isEmpty()) {
            throw new RuntimeException("服务不可用");
        }

        //TODO get first element for now, two-step need rebalanced
        ServiceMetaInfo serviceMetaInfo = serviceMetaInfos.get(0);
        Serializer serializer = SerializerFactory.newInstance("jdk");

        byte[] bytes = serializer.serialize(requestBody);

        try (HttpResponse response = HttpRequest.post(serviceMetaInfo.getFullyAddress()).body(bytes).execute()) {
            byte[] bodyBytes = response.bodyBytes();
            RpcResponse deserialize = serializer.deserialize(bodyBytes, RpcResponse.class);
            return deserialize.getData();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("异常");
        }
    }
}
