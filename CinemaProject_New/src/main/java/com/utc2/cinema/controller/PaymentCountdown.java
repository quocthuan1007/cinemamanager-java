package com.utc2.cinema.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private static final int COUNTDOWN_SECONDS = 60;
    private int remainingSeconds = COUNTDOWN_SECONDS;
    private Timeline timeline;
    private String orderId;

    private Label countdownLabel;

    public void start(Stage stage, String paymentUrl, String orderId) {
        this.orderId = orderId;

        countdownLabel = new Label("Thời gian chờ: " + remainingSeconds + " giây");
        VBox root = new VBox(20, countdownLabel);
        root.setStyle("-fx-padding: 30; -fx-alignment: center;");
        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("Thanh toán");
        stage.setScene(scene);
        stage.show();

        // Mở trình duyệt WebView để người dùng thanh toán
        openPaymentPage(paymentUrl);

        // Bắt đầu countdown
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

            // Cứ mỗi 3 giây kiểm tra thanh toán
            if (remainingSeconds % 3 == 0) {
                checkPaymentStatus();
            }

            if (remainingSeconds <= 0) {
                timeline.stop();
                showFailure();
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
                showSuccess();
            }
        });

        new Thread(task).start();
    }

    private void showSuccess() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thanh toán thành công");
            alert.setContentText("Giao dịch đã được xác nhận thành công!");
            alert.showAndWait();
        });
    }

    private void showFailure() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thanh toán thất bại");
            alert.setContentText("Hết thời gian chờ mà giao dịch chưa hoàn tất.");
            alert.showAndWait();
        });
    }
}

