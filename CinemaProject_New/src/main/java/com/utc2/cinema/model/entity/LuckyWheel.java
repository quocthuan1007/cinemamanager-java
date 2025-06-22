package com.utc2.cinema.model.entity;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class LuckyWheel extends Application {

    private final String[] prizes = {"GOLD", "SILVER", "BRONZE", "MISS"};
    private final Color[] colors = {Color.GOLD, Color.SILVER, Color.SANDYBROWN, Color.GRAY};
    private final Random random = new Random();
    private boolean isSpinning = false;

    @Override
    public void start(Stage stage) {
        Label resultLabel = new Label("🎁 Nhấn QUAY để thử vận may!");
        resultLabel.setFont(new Font(22));
        resultLabel.setTextFill(Color.DARKBLUE);

        Button spinButton = new Button("🎯 QUAY!");
        spinButton.setFont(new Font(18));
        spinButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        spinButton.setOnAction(e -> handleSpin(spinButton, resultLabel));

        VBox root = new VBox(30, resultLabel, spinButton);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 400, 250);

        stage.setTitle("Vòng Quay May Mắn");
        stage.setScene(scene);
        stage.show();
    }

    private void handleSpin(Button spinButton, Label resultLabel) {
        if (isSpinning) return;
        isSpinning = true;
        spinButton.setDisable(true);

        Timeline timeline = new Timeline();
        int durationMs = 10*1000;
        int intervalMs = 100;
        int totalSteps = durationMs / intervalMs;
        final String[] finalResult = {""};

        for (int i = 0; i < totalSteps; i++) {
            KeyFrame frame = new KeyFrame(Duration.millis(i * intervalMs), e -> {
                int index = random.nextInt(prizes.length);
                finalResult[0] = prizes[index];
                resultLabel.setText("🎲 " + prizes[index]);
                resultLabel.setTextFill(colors[index]);
            });
            timeline.getKeyFrames().add(frame);
        }

        timeline.setOnFinished(e -> {
            String result = finalResult[0];
            if (!result.equals("MISS")) {
                resultLabel.setText("🎉 Bạn trúng: " + result);
                resultLabel.setTextFill(Color.GREEN);
                // 👉 ở đây bạn có thể gọi DAO để lưu kết quả nếu muốn
            } else {
                resultLabel.setText("😢 Không trúng thưởng.");
                resultLabel.setTextFill(Color.DARKGRAY);
            }

            spinButton.setDisable(false);
            isSpinning = false;
        });

        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
