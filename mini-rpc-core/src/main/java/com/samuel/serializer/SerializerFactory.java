package com.samuel.serializer;

public class SerializerFactory {

    public static Serializer newInstance(String type) {
        if (type.equals("jdk")) {
            return new JdkSerializer();
        }

        return null;
    }
}
