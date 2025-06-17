package com.utc2.cinema.model.entity;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.atomic.AtomicBoolean;

public class CustomAlert {

    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void showInfo(String title, String header, String content) {
        showCustomAlert(title, header, content, "alert", false, null);
    }

    public static void showError(String title, String header, String content) {
        showCustomAlert(title, header, content, "alert-error", false, null);
    }

    public static boolean showConfirmation(String title, String header, String content) {
        AtomicBoolean confirmed = new AtomicBoolean(false);
        showCustomAlert(title, header, content, "alert-confirm", true, confirmed);
        return confirmed.get();
    }

    private static void showCustomAlert(String title, String header, String content,
                                        String styleClass, boolean isConfirm, AtomicBoolean confirmed) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        Label headerLabel = new Label(header);
        headerLabel.getStyleClass().add("header-panel");

        Label contentLabel = new Label(content);
        contentLabel.getStyleClass().addAll("content", "label");

        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.getStyleClass().add("button-bar");

        if (isConfirm && confirmed != null) {
            Button okBtn = new Button("OK");
            Button cancelBtn = new Button("Hủy");

            okBtn.setOnAction(e -> {
                confirmed.set(true);
                stage.close();
            });

            cancelBtn.setOnAction(e -> {
                confirmed.set(false);
                stage.close();
            });

            okBtn.getStyleClass().add("ok-button");
            cancelBtn.getStyleClass().add("cancel-button");
            buttonBar.getChildren().addAll(okBtn, cancelBtn);
        } else {
            Button okBtn = new Button("OK");
            okBtn.setOnAction(e -> stage.close());
            okBtn.getStyleClass().add("button");
            buttonBar.getChildren().add(okBtn);
        }

        VBox root = new VBox(headerLabel, contentLabel, buttonBar);
        root.setSpacing(12);
        root.setPadding(new Insets(20));
        root.getStyleClass().add(styleClass);

        // Kéo rê cửa sổ
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

        stage.setScene(scene);
        stage.setTitle(title);
        stage.showAndWait();
    }
    public static void showError(String title, String content) {
        showCustomAlert(title, "", content, "alert-error", false, null);
    }

    public static void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
