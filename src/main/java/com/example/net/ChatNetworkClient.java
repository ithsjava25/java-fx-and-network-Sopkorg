package com.example.net;

import java.io.Closeable;


public interface ChatNetworkClient {


    void send(String baseUrl, String topic, String message) throws Exception;


    Subscription subscribe(String baseUrl, String topic, MessageHandler handler);


    interface Subscription extends Closeable {
        @Override
        void close();
        boolean isOpen();
    }


    @FunctionalInterface
    interface MessageHandler {
        void onMessage(String message);
    }
}