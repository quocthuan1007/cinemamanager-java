package com.utc2.cinema.controller;

import com.google.gson.JsonObject;
import com.utc2.cinema.model.entity.Vnpay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import javax.servlet.ServletException;

public class vnpayQuery {

    public static Vnpay checkDeal(String order_id, String trans_date) throws ServletException, IOException {
        String vnp_RequestId = Config.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TmnCode = Config.vnp_TmnCode;
        String vnp_TxnRef = order_id;
        String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;

        String vnp_TransDate;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            vnp_TransDate = outputFormat.format(inputFormat.parse(trans_date));
        } catch (Exception e) {
            throw new ServletException("Định dạng trans_date không hợp lệ. Phải là dd/MM/yyyy HH:mm:ss", e);
        }

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        String vnp_IpAddr = "127.0.0.1";

        JsonObject vnp_Params = new JsonObject();
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransDate);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data = String.join("|",
                vnp_RequestId,
                vnp_Version,
                vnp_Command,
                vnp_TmnCode,
                vnp_TxnRef,
                vnp_TransDate,
                vnp_CreateDate,
                vnp_IpAddr,
                vnp_OrderInfo
        );

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hash_Data);
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        URL url = new URL(Config.vnp_ApiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(vnp_Params.toString());
            wr.flush();
        }

        int responseCode = con.getResponseCode();

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String output;
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
        }

        // Trả về đối tượng Vnpay chứa mã phản hồi và dữ liệu trả về
        return new Vnpay(responseCode, response.toString());
    }


    public static void main(String[] args) throws ServletException, IOException {
        try {
            Vnpay result = checkDeal("32811367", "02/06/2025 19:39:18");
            System.out.println("Mã phản hồi: " + result.getResponseCode());
            System.out.println("Dữ liệu phản hồi: " + result.getResponseData());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
