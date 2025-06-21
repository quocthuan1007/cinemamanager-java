package com.utc2.cinema.model.entity;

import java.util.Random;

public class CreateOTP {
    public static String generateOTP(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
