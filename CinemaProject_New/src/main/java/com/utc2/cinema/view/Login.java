package com.utc2.cinema.view;

import com.utc2.cinema.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Login extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("/FXML/Login.fxml"));
            LoginController control = new LoginController();
            fxmlLoader.setController(control);
            Pane root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            stage.setTitle("Hello !");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
