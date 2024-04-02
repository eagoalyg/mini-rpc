package com.samuel.bootstrap;

import com.samuel.SRpcApplication;
import com.samuel.annotation.EnableRpc;
import com.samuel.annotation.RpcClient;
import com.samuel.server.HttpServer;
import com.samuel.server.VertxHttpServer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableRpc.class.getName());
        boolean enableRpc = false;
        if (annotationAttributes != null) {
            enableRpc = (boolean) annotationAttributes.get("value");
        }

        SRpcApplication.init();


        if (enableRpc) {
            Thread http = new Thread(() -> {
                HttpServer httpServer = new VertxHttpServer();
                //TODO get from config
                httpServer.doStart(8888);
            });
            http.start();
        }
    }
}
