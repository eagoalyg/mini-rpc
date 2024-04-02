package com.samuel.server;

import com.samuel.model.RpcRequest;
import com.samuel.model.RpcResponse;
import com.samuel.registry.LocalRegistry;
import com.samuel.serializer.JdkSerializer;
import com.samuel.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class HttpRequestHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        final Serializer serializer = new JdkSerializer();

        System.out.println("Received request: " + request.method() + " " + request.uri());

        request.bodyHandler(body -> {
            byte[] data = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(data, RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            RpcResponse response = new RpcResponse();
            if (rpcRequest == null) {
                response.setException("request is null");
                doResponse(request, serializer, response);
                return;
            }

            try {
                Class<?> targetClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = targetClass.getMethod(rpcRequest.getMethod(), rpcRequest.getParameterType());
                Object result = method.invoke(targetClass.newInstance(), rpcRequest.getArgs());

                response.setData(result);
                response.setMessage("succeed");
            } catch (Exception e) {
                // log
                response.setMessage(e.getMessage());
                response.setException(e.getMessage());
            }
            doResponse(request, serializer, response);
        });
    }

    private void doResponse(HttpServerRequest request, Serializer serializer, RpcResponse response) {
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");

        try {
            byte[] serialize = serializer.serialize(response);
            httpServerResponse.end(Buffer.buffer(serialize));
        } catch (IOException e) {
            // log
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
