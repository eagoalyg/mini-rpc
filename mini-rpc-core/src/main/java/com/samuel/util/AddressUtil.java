package com.samuel.util;

import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressUtil {

    //TODO
    @Value("${server.port:8080}")
    private static String port = "8888";

    public static String generateLocalAddress() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        return localHost.getHostAddress() + ":" + port;
    }

    public static String getLocalHost() {
        String localhost = "127.0.0.1";
        try {
            localhost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            //log
        }

        return localhost;
    }
}
