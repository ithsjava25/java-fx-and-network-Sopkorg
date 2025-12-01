package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ChatFX.class.getResource("chat-view.fxml"));
        Parent root = loader.load();

        ChatModel model = new ChatModel();
        ChatController controller = loader.getController();
        controller.setModel(model);

        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Min ChatApp (lokal)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}