package com.samuel.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;

public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
        io.vertx.core.http.HttpServer server = vertx.createHttpServer(options);
        server.requestHandler(new HttpRequestHandler());

        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("rpc http-server now is listening on: " + port);
            } else {
                System.out.println("http-server run fail: " + result.cause());
            }
        });
    }
}
