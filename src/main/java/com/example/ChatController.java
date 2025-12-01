package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController {

    private ChatModel model;

    @FXML
    private ListView<String> messageList;

    @FXML
    private TextField inputField;

    @FXML
    private TextField topicField;

    @FXML
    private Button sendButton;

    @FXML
    private Button connectButton;

    @FXML
    private Button disconnectButton;

    public void setModel(ChatModel model) {
        this.model = model;
        messageList.setItems(model.getMessages());
    }

    @FXML
    private void onSend() {
        model.sendMessage(inputField.getText());
        inputField.clear();
    }

    @FXML
    private void onConnect() {
        String topic = topicField.getText();
        model.connect(topic);
    }

    @FXML
    private void onDisconnect() {
        model.disconnect();
    }
}