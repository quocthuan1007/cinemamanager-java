package com.utc2.cinema.model.entity;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailOTP {
    public static void sendEmail(String to, String otp) throws Exception {
        String from = "minecraftchannel510@gmail.com";
        String password = "mlzf kxqe glfw wfcp";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject("Mã xác thực OTP");
        msg.setText("Mã xác thực của bạn là: " + otp);

        Transport.send(msg);
        System.out.println("Đã gửi OTP qua email.");
    }
    public static void main(String[] args) throws Exception {
        String otp = CreateOTP.generateOTP(6);
        EmailOTP.sendEmail("lebao.071295@gmail.com", otp);
    }
}

