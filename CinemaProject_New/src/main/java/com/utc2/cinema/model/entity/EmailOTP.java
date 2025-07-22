package com.utc2.cinema.model.entity;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailOTP {
    public static void sendEmail(String to, String title, String content) throws Exception {
        String from = "minecraftchannel510@gmail.com";
        String password = "mlzf kxqe glfw wfcp"; // App password từ Google

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // ✅ Bypass xác minh SSL

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from, "BTH-CINEMA"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(title);

        String htmlContent = "<!DOCTYPE html>"
                + "<html><body style='font-family:Arial,sans-serif;'>"
                + "<div style='max-width:500px;"
                + "margin:auto;"
                + "padding:20px;"
                + "border:1px solid #ccc;"
                + "border-radius:10px;"
                + "background-color:#61C17E;'>"
                + "<h2 style='color:#333;'>Thông báo từ hệ thống</h2>"
                + "<p style='font-size:20px;color:#FFFFFF;'>" + content + "</p>"
                + "<br><p style='font-size:14px;color:#999;'>Nếu bạn không yêu cầu, hãy bỏ qua email này.</p>"
                + "</div></body></html>";

        msg.setContent(htmlContent, "text/html; charset=UTF-8");

        Transport.send(msg);
        System.out.println("✅ Email đã gửi tới " + to);
    }
}
