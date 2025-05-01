package com.utc2.cinema.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Kiểm tra độ dài mật khẩu (ít nhất 8 ký tự)
    public static boolean isValidLength(String password) {
        return password != null && password.length() >= 8;
    }

    // Kiểm tra mật khẩu có chứa ít nhất một chữ cái viết hoa
    public static boolean hasUpperCase(String password) {
        return password != null && password.matches(".*[A-Z].*");
    }

    // Kiểm tra mật khẩu có chứa ít nhất một chữ cái viết thường
    public static boolean hasLowerCase(String password) {
        return password != null && password.matches(".*[a-z].*");
    }

    // Kiểm tra mật khẩu có chứa ít nhất một chữ số
    public static boolean hasDigit(String password) {
        return password != null && password.matches(".*[0-9].*");
    }

    // Kiểm tra mật khẩu có chứa ít nhất một ký tự đặc biệt
    public static boolean hasSpecialCharacter(String password) {
        return password != null && password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 là độ mạnh
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    // Kiểm tra mật khẩu có hợp lệ không (độ dài, chữ hoa, chữ thường, chữ số, ký tự đặc biệt)
    public static boolean isValidPassword(String password) {
        return password != null &&
                isValidLength(password) &&
                hasUpperCase(password) &&
                hasLowerCase(password) &&
                hasDigit(password) &&
                hasSpecialCharacter(password);
    }
}
