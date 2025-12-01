package com.example;

import com.example.net.ChatNetworkClient;
import com.example.net.NtfyHttpClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatModel {

    private final ObservableList<String> messages =
            FXCollections.observableArrayList();

    private final ChatNetworkClient client;
    private ChatNetworkClient.Subscription subscription;

    private String baseUrl = System.getenv().getOrDefault(
            "CHAT_BACKEND_URL",
            "https://ntfy.fungover.org"
    );
    private String topic = "testtopic";

    public ChatModel() {
        this(new NtfyHttpClient());
    }

    public ChatModel(ChatNetworkClient client) {
        this.client = client;
    }

    public ObservableList<String> getMessages() {
        return messages;
    }

    public void connect(String topic) {
        if (topic != null && !topic.isBlank()) {
            this.topic = topic;
        }

        messages.add("[CONNECTING till topic: " + this.topic + "]");

        subscription = client.subscribe(baseUrl, this.topic, msg -> {
            messages.add("Server: " + msg);
        });

        messages.add("[CONNECTED]");
    }

    public void disconnect() {
        if (subscription != null) {
            subscription.close();
            subscription = null;
            messages.add("[DISCONNECTED]");
        }
    }

    public void sendMessage(String text) {
        if (text == null || text.isBlank()) {
            return;
        }

        messages.add("Du: " + text);

        try {
            client.send(baseUrl, topic, text);
        } catch (Exception e) {
            messages.add("[Fel vid utskick: " + e.getMessage() + "]");
        }
    }
}