package com.utc2.cinema.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

public class test {

    @FXML private TextField orderIdField;
    @FXML private TextField amountField;
    @FXML private Button generateQRButton;
    @FXML private ImageView qrImageView;
    @FXML private Label paymentStatusLabel;

    // VNPay sandbox credentials
    private final String vnp_TmnCode = "2QXUI4J4";
    private final String vnp_HashSecret = "SECRETKEY123456789";
    private final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private final String vnp_ReturnUrl = "https://sandbox.vnpayment.vn/paymentv2/ReturnUrl";

    @FXML
    public void initialize() {
        generateQRButton.setOnAction(e -> createPayment());
    }

    private void createPayment() {
        try {
            String orderId = orderIdField.getText();
            String amountStr = amountField.getText();
            if (orderId.isEmpty() || amountStr.isEmpty()) {
                paymentStatusLabel.setText("Vui lòng nhập đầy đủ thông tin.");
                return;
            }

            long amount = Long.parseLong(amountStr) * 100; // VNPay dùng đơn vị nhỏ (x100)
            String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            // Kiểm tra lại các tham số trước khi gửi
            System.out.println("OrderId: " + orderId);
            System.out.println("Amount: " + amount);
            System.out.println("CreateDate: " + createDate);

            // Thêm tham số cần thiết
            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", vnp_TmnCode);
            params.put("vnp_Amount", String.valueOf(amount));
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", orderId);
            params.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
            params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            params.put("vnp_IpAddr", "127.0.0.1"); // Địa chỉ IP của người thanh toán
            params.put("vnp_CreateDate", createDate);

            // Kiểm tra lại tham số trước khi tiếp tục
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // Sắp xếp các tham số theo tên và mã hóa URL
            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            // Tạo chuỗi dữ liệu cần thiết để tính toán hash và chuỗi query
            for (int i = 0; i < fieldNames.size(); i++) {
                String key = fieldNames.get(i);
                String value = URLEncoder.encode(params.get(key), StandardCharsets.UTF_8.toString());

                hashData.append(key).append('=').append(value);
                query.append(key).append('=').append(value);
                if (i != fieldNames.size() - 1) {
                    hashData.append('&');
                    query.append('&');
                }
            }

            // Tính toán HMAC-SHA512
            String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(secureHash);

            // Kiểm tra lại URL thanh toán
            System.out.println("Payment URL: " + vnp_PayUrl + "?" + query);

            // Tạo QR code
            generateQRCode(vnp_PayUrl + "?" + query);
            paymentStatusLabel.setText("Mã QR đã được tạo. Quét để thanh toán.");
        } catch (Exception ex) {
            ex.printStackTrace();
            paymentStatusLabel.setText("Có lỗi xảy ra.");
        } finally {
            generateQRButton.setDisable(false); // Re-enable button after processing
        }
    }

    private void generateQRCode(String data) throws Exception {
        // Disable the button while processing
        generateQRButton.setDisable(true);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        Image fxImage = SwingFXUtils.toFXImage(qrImage, null);
        qrImageView.setImage(fxImage);
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(bytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hash.append('0');
            hash.append(hex);
        }
        return hash.toString();
    }
}
