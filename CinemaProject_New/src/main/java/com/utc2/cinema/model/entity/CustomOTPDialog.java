package com.utc2.cinema.model.entity;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.util.Optional;

public class CustomOTPDialog {

    private static double xOffset = 0;
    private static double yOffset = 0;

    public static Optional<String> show(String email) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        Label headerLabel = new Label("OTP đã được gửi đến:");
        headerLabel.getStyleClass().add("header-panel");

        Label emailLabel = new Label(email);
        emailLabel.getStyleClass().add("email-label");

        TextField otpField = new TextField();
        otpField.setPromptText("Nhập 6 chữ số");
        otpField.setMaxWidth(200);
        otpField.getStyleClass().add("otp-field");

        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.getStyleClass().add("button-bar");

        Button okBtn = new Button("Xác nhận");
        Button cancelBtn = new Button("Hủy");

        buttonBar.getChildren().addAll(okBtn, cancelBtn);
        okBtn.getStyleClass().add("ok-button");
        cancelBtn.getStyleClass().add("cancel-button");

        VBox root = new VBox(10, headerLabel, emailLabel, otpField, buttonBar);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("alert");

        // Kéo cửa sổ
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(CustomAlert.class.getResource("/CSS/alert-style.css").toExternalForm());

        final Optional<String>[] result = new Optional[]{Optional.empty()};

        okBtn.setOnAction(e -> {
            result[0] = Optional.ofNullable(otpField.getText().trim());
            stage.close();
        });

        cancelBtn.setOnAction(e -> {
            result[0] = Optional.empty();
            stage.close();
        });

        stage.setScene(scene);
        stage.setTitle("Xác thực OTP");
        stage.showAndWait();

        return result[0];
    }
}
