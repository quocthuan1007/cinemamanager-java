package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.CustomAlert;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PaymentCountdown {

    private static final int COUNTDOWN_SECONDS = 120;
    private int remainingSeconds = COUNTDOWN_SECONDS;
    private Timeline timeline;
    private String orderId;

    private Label countdownLabel;

    public void start(Stage stage, String paymentUrl, String orderId) {
        this.orderId = orderId;

        countdownLabel = new Label("⏳ Thời gian chờ: " + remainingSeconds + " giây");
        countdownLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #007bff;");
        Button cancelButton = new Button("❌ Huỷ thanh toán");
        cancelButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20; -fx-background-radius: 6;");
        cancelButton.setOnAction(e -> {
            if (timeline != null) timeline.stop();
            stage.close();
            CustomAlert.showError("Bạn vừa huỷ thanh toán","","Vui lòng thanh toán lại!");
        });

        VBox contentBox = new VBox(20, countdownLabel, cancelButton);
        contentBox.setStyle("-fx-padding: 20; -fx-background-color: #ffffff; -fx-border-color: #007bff; -fx-border-radius: 10; -fx-background-radius: 10;");
        contentBox.setPrefSize(433, 200);
        contentBox.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(contentBox);
        stage.setTitle("Thanh toán VNPay");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        openPaymentPage(paymentUrl);
        startCountdown();
    }


    private void openPaymentPage(String url) {
        Platform.runLater(() -> {
            WebView webView = new WebView();
            webView.getEngine().load(url);

            Stage stage = new Stage();
            stage.setTitle("VNPay Thanh Toán");
            stage.setScene(new Scene(webView, 1000, 700));
            stage.show();
        });
    }

    private void startCountdown() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            remainingSeconds--;
            countdownLabel.setText("Thời gian chờ: " + remainingSeconds + " giây");

            if (remainingSeconds % 3 == 0) {
                checkPaymentStatus();
            }

            if (remainingSeconds <= 0) {
                timeline.stop();
                CustomAlert.showError("Hết thời gian thanh toán!","","Vui lòng thanh toán lại!");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void checkPaymentStatus() {
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                URL url = new URL("http://localhost:8080/check_payment?orderId=" + orderId);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String response = in.readLine();
                in.close();
                return response;
            }
        };

        task.setOnSucceeded(event -> {
            String response = task.getValue();
            if ("PAID".equalsIgnoreCase(response)) {
                timeline.stop();
                CustomAlert.showInfo("Thanh toán thành công","","Hoàn tất");
            }
        });

        new Thread(task).start();
    }
}

