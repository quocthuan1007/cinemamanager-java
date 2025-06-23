package com.utc2.cinema.model.entity;

import com.utc2.cinema.dao.InventoryDao;
import com.utc2.cinema.service.AccountService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.Random;
import java.util.function.Consumer;

public class LuckyWheel {

    private static final String[] prizes = {"GOLD", "SILVER", "BRONZE", "MISS"};
    private static final Color[] colors = {Color.GOLD, Color.SILVER, Color.SANDYBROWN, Color.GRAY};
    private static final Random random = new Random();

    private static boolean isSpinning = false; // Trạng thái đang quay

    /**
     * Hiển thị vòng quay và trả kết quả qua callback.
     * @param callback Hàm nhận kết quả khi quay xong.
     */
    public static void start(Consumer<String> callback) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // chặn các cửa sổ khác cho đến khi quay xong

            Label resultLabel = new Label("🎁 Nhấn QUAY để thử vận may!");
            resultLabel.setFont(new Font(22));
            resultLabel.setTextFill(Color.DARKBLUE);

            Button spinButton = new Button("🎯 QUAY!");
            spinButton.setFont(new Font(18));
            spinButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

            VBox root = new VBox(30, resultLabel, spinButton);
            root.setAlignment(Pos.CENTER);
            Scene scene = new Scene(root, 400, 250);

            stage.setTitle("Vòng Quay May Mắn");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest((WindowEvent event) -> {
                if (isSpinning) {
                    event.consume();
                    resultLabel.setText("🚫 Không thể tắt khi đang quay!");
                    resultLabel.setTextFill(Color.RED);
                }
            });

            spinButton.setOnAction(e -> handleSpin(spinButton, resultLabel, stage, callback));
        });
    }

    private static void handleSpin(Button spinButton, Label resultLabel, Stage stage, Consumer<String> callback) {
        spinButton.setDisable(true);
        isSpinning = true;

        Timeline timeline = new Timeline();
        int durationMs = 4000;
        int intervalMs = 100;
        int totalSteps = durationMs / intervalMs;

        final int[] currentIndex = {0};

        for (int i = 0; i < totalSteps; i++) {
            KeyFrame frame = new KeyFrame(Duration.millis(i * intervalMs), e -> {
                int index = random.nextInt(prizes.length);
                currentIndex[0] = index;
                resultLabel.setText("🎲 " + prizes[index]);
                resultLabel.setTextFill(colors[index]);
            });
            timeline.getKeyFrames().add(frame);
        }

        timeline.setOnFinished(e -> {
            int resultIndex = getLuckyResult();
            String result = prizes[resultIndex];
            InventoryDao.useVoucher(AccountService.getDataByEmail(UserSession.getInstance().getEmail()).getId() , "ticket",-1);
            if (!result.equals("MISS")) {
                resultLabel.setText("🎉 Bạn trúng: " + result);
                resultLabel.setTextFill(Color.GREEN);
                switch (resultIndex)
                {
                    case 0://gold
                    {
                        InventoryDao.useVoucher(AccountService.getDataByEmail(UserSession.getInstance().getEmail()).getId() , "gold",1);
                    }
                    case 1://silver
                    {
                        InventoryDao.useVoucher(AccountService.getDataByEmail(UserSession.getInstance().getEmail()).getId() , "silver",1);
                    }
                    case 2://bronze
                    {
                        InventoryDao.useVoucher(AccountService.getDataByEmail(UserSession.getInstance().getEmail()).getId() , "bronze",1);
                    }
                }
            } else {
                resultLabel.setText("😢 Không trúng thưởng.");
                resultLabel.setTextFill(Color.DARKGRAY);
            }

            // Đóng cửa sổ sau 2 giây và trả kết quả qua callback
            Timeline closeTimeline = new Timeline(new KeyFrame(Duration.seconds(2), evt -> {
                isSpinning = false; // Cho phép tắt sau khi quay xong
                stage.close();
                if (callback != null) callback.accept(result);
            }));
            closeTimeline.play();
        });

        timeline.play();
    }

    /**
     * Hàm quay theo tỉ lệ:
     * GOLD: 1%
     * SILVER: 5%
     * BRONZE: 20%
     * MISS: 74%
     */
    public static int getLuckyResult() {
        double chance = random.nextDouble() * 100;

        if (chance < 1) return 0;         // GOLD 1%
        else if (chance < 6) return 1;    // SILVER 5%
        else if (chance < 26) return 2;   // BRONZE 20%
        else return 3;                    // MISS 74%
    }
}
