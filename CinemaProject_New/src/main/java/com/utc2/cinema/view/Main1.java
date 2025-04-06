package com.utc2.cinema.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/ShowFilm.fxml"));
        Scene scene = new Scene(root, 1160, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quản Lý Rạp Chiếu Phim");
        primaryStage.show();
    }
}
