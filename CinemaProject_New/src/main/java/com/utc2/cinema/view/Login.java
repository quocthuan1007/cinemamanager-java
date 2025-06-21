package com.utc2.cinema.view;

import com.utc2.cinema.controller.LoginController;
import com.utc2.cinema.dao.BillDao;
import com.utc2.cinema.model.entity.Bill;
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
import org.springframework.web.bind.annotation.*;

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
        @GetMapping("/payment-status")
        @ResponseBody
        public String checkPaymentStatus(@RequestParam("billId") int billId) {
            String status = BillDao.getBillById(billId).getBillStatus();
            return status;
        }

        @GetMapping("/vnpay_return")
        @ResponseBody
        public String vnpayReturn(@RequestParam Map<String, String> params, @RequestParam("billId") int billId) {
            System.out.println("VNPay callback về với billId = " + billId);


            Bill bill = BillDao.getBillById(billId);

            if (bill == null) {
                return "FAIL: Bill không tồn tại";
            }

            if (!"PENDING".equalsIgnoreCase(bill.getBillStatus().trim())) {
                return "FAIL: Bill không còn ở trạng thái PENDING";
            }
            Bill bill = BillDao.getBillById(billId);
            // Nếu hóa đơn null hoac khac pending
            if (bill == null || !"PENDING".equals(bill.getBillStatus())) {
                return "FAIL: Có lỗi xảy ra";
            }
            boolean updated = BillDao.updateBillStatus(billId, "PAID");
            return updated ? "SUCCESS" : "FAIL";
        }
    }


}
