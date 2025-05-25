package com.utc2.cinema.view;

import com.utc2.cinema.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED); // Bỏ khung cửa sổ mặc định

        try {
            // Load nội dung từ FXML
            FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("/FXML/Login.fxml"));
            LoginController control = new LoginController();
            fxmlLoader.setController(control);
            Pane content = fxmlLoader.load();

            // Gộp content và các nút vào StackPane (nút nằm trên content)
            StackPane root = new StackPane(content);

            // Cho phép kéo cửa sổ
            root.setOnMousePressed(e -> {
                // Chỉ bắt khi không nhấn vào control con (button, textfield...)

                    xOffset = e.getSceneX();
                    yOffset = e.getSceneY();

            });

            root.setOnMouseDragged(e -> {
                    stage.setX(e.getScreenX() - xOffset);
                    stage.setY(e.getScreenY() - yOffset);

            });


            Scene scene = new Scene(root, 954, 562);
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
