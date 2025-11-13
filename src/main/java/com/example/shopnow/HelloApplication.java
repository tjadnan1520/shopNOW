package com.example.shopnow;

import com.example.shopnow.controller.EntryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/EntryPage.fxml"));

        Scene scene = new Scene(loader.load());

        EntryController entryController = loader.getController();
        entryController.setStage(stage);

        stage.setTitle(entryController.getTitle());
        stage.setScene(scene);
        stage.setWidth(1415);
        stage.setHeight(700);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}