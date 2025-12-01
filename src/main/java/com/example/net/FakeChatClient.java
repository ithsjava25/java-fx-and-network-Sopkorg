package com.example.net;


public class FakeChatClient implements ChatNetworkClient {

    @Override
    public void send(String baseUrl, String topic, String message) {
        System.out.println("[FAKE SEND] baseUrl=" + baseUrl +
                " topic=" + topic +
                " message=" + message);
    }

    @Override
    public Subscription subscribe(String baseUrl, String topic, MessageHandler handler) {
        System.out.println("[FAKE SUBSCRIBE] baseUrl=" + baseUrl + " topic=" + topic);

        // Skapa en fejk-subscription
        return new Subscription() {

            private boolean open = true;

            @Override
            public void close() {
                open = false;
                System.out.println("[FAKE SUBSCRIPTION CLOSED]");
            }

            @Override
            public boolean isOpen() {
                return open;
            }
        };
    }
}