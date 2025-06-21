package com.utc2.cinema.model.entity;

import java.security.SecureRandom;

public class CreateRandomPassword {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_+=<>?";

    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    public static String generateRandomPassword(int length) {
        if (length < 6) length = 6;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Đảm bảo ít nhất 1 ký tự của mỗi loại
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Thêm ngẫu nhiên các ký tự còn lại
        for (int i = 4; i < length; i++) {
            password.append(ALL.charAt(random.nextInt(ALL.length())));
        }

        // Xáo trộn chuỗi để tránh cố định các ký tự đầu
        return shuffleString(password.toString(), random);
    }

    private static String shuffleString(String input, SecureRandom random) {
        char[] a = input.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // hoán đổi
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        return new String(a);
    }
}

