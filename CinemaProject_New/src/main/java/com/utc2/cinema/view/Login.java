package com.utc2.cinema.view;

import com.utc2.cinema.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
        Label label = new Label("BTH Cinema");
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 3 0 0 10;");

        Button closeBtn = new Button("X");
        closeBtn.setOnAction(e -> stage.close());
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        Button minBtn = new Button("_");
        minBtn.setOnAction(e -> stage.setIconified(true));
        minBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox titleBar = new HBox(10, label, spacer, minBtn, closeBtn);
        titleBar.setStyle("-fx-background-color: #8B0A50;");
        titleBar.setOnMousePressed(e->{
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        titleBar.setOnMouseDragged(e->{
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        try {
            // Load nội dung Login.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("/FXML/Login.fxml"));
            LoginController control = new LoginController();
            fxmlLoader.setController(control);
            Pane content = fxmlLoader.load();

            // Gộp tiêu đề và nội dung vào một VBox
            VBox root = new VBox(titleBar, content);
            Scene scene = new Scene(root, 600, 400);

            stage.setTitle("BTH Cinema - Đăng nhập");
            stage.setResizable(false);
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
