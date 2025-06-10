package com.utc2.cinema.view;

import com.utc2.cinema.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@SpringBootApplication
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
            StackPane root = new StackPane(content);

            root.setOnMousePressed(e -> {

                    xOffset = e.getSceneX();
                    yOffset = e.getSceneY();

            });

            root.setOnMouseDragged(e -> {
                    stage.setX(e.getScreenX() - xOffset);
                    stage.setY(e.getScreenY() - yOffset);

            });

            stage.setOnCloseRequest(event -> {
                System.out.println("🔄 Shutting down...");
                System.exit(0); // Tắt toàn bộ JVM - kill tất cả threads
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
        SpringApplication.run(Login.class, args);
        launch(args);
    }
    @Controller
    public class Api {
        @RequestMapping(value ="/vnpay_return", method = RequestMethod.GET)
        @ResponseBody
        public String getBuilding(@RequestParam Map<String, String> apiContent) {
            System.out.println("All parameters: " + apiContent);

            String vnp_Amount = apiContent.get("vnp_Amount");
            String vnp_BankCode = apiContent.get("vnp_BankCode");
            String vnp_ResponseCode = apiContent.get("vnp_ResponseCode");
            String vnp_TransactionStatus = apiContent.get("vnp_TransactionStatus");
            String vnp_SecureHash = apiContent.get("vnp_SecureHash");

            System.out.println("Amount: " + vnp_Amount);
            System.out.println("Response Code: " + vnp_ResponseCode);
            return "Hello";
        }
    }


}
