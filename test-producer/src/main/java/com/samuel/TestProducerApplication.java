package com.samuel;

import com.samuel.annotation.EnableRpc;
import com.samuel.bootstrap.RpcInitBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import(RpcInitBootstrap.class)
@EnableRpc(value = true)
@SpringBootApplication
public class TestProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestProducerApplication.class, args);
    }
}
