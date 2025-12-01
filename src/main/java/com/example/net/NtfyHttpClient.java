package com.example.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class NtfyHttpClient implements ChatNetworkClient {

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void send(String baseUrl, String topic, String message) throws Exception {
        String url = baseUrl + "/" + topic;

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .uri(URI.create(url))
                .header("Cache", "no")
                .build();

        http.send(request, HttpResponse.BodyHandlers.discarding());
    }

    @Override
    public Subscription subscribe(String baseUrl, String topic, MessageHandler handler) {
        String url = baseUrl + "/" + topic + "/json";

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        CompletableFuture<Void> future = http
                .sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body().forEach(line -> {
                    try {
                        var json = mapper.readTree(line);

                        if (json.has("event")
                                && "message".equals(json.get("event").asText())
                                && json.has("message")) {

                            String msg = json.get("message").asText();
                            Platform.runLater(() -> handler.onMessage(msg));
                        }

                    } catch (Exception e) {
                        System.out.println("[NTFY PARSE ERROR] " + e);
                    }
                }));

        return new NtfySubscription(future);
    }

    private static class NtfySubscription implements Subscription {
        private final CompletableFuture<?> future;
        private boolean open = true;

        private NtfySubscription(CompletableFuture<?> future) {
            this.future = future;
        }

        @Override
        public void close() {
            open = false;
            future.cancel(true);
        }

        @Override
        public boolean isOpen() {
            return open && !future.isDone();
        }
    }
}