package com.utc2.cinema.utils;

import com.utc2.cinema.controller.MainManagerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TestManager extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tải tệp FXML

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainManager.fxml"));
            MainManagerController a = new MainManagerController();
            loader.setController(a);

            AnchorPane root = loader.load();

            // Thiết lập cảnh (Scene)
            Scene scene = new Scene(root, 1160, 800);
            primaryStage.setTitle("Cinema Manager");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
