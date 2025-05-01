package com.utc2.cinema.utils;

public class ValidationUtils {
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("0[0-9]{9}");
    }
}
