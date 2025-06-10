package com.utc2.cinema.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet("/vnpay_return")
public class vnpayReturn extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Context path: " + request.getContextPath());

        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> fields = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);

            // Bỏ qua các field không cần ký lại
            if (!paramName.equals("vnp_SecureHash") && !paramName.equals("vnp_SecureHashType")) {
                fields.put(paramName, paramValue);
            }
        }

        // Lấy secure hash từ VNP trả về
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        // Tạo chuỗi dữ liệu để ký lại
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String key = fieldNames.get(i);
            String value = fields.get(key);
            if (value != null && value.length() > 0) {
                hashData.append(URLEncoder.encode(key, StandardCharsets.UTF_8.toString()));
                hashData.append('=');
                hashData.append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                }
            }
        }

        String signValue = Config.hmacSHA512(Config.secretKey, hashData.toString());

        if (signValue.equals(vnp_SecureHash)) {
            // Thành công rồi!
            String transactionStatus = request.getParameter("vnp_TransactionStatus");
            boolean transSuccess = "00".equals(transactionStatus);

            if (transSuccess) {
                System.out.println("oks"); // ✅ In ra "oks"
            }

            request.setAttribute("transResult", transSuccess);
            request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
        } else {
            System.out.println("GD KHÔNG HỢP LỆ (Invalid Signature)");
            response.getWriter().println("Invalid signature.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("cac");
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("cac");
        processRequest(request, response);
    }

}
