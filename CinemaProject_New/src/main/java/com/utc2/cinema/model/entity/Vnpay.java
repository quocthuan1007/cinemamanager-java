package com.utc2.cinema.model.entity;

public class Vnpay {
    private int responseCode;
    private String responseData;

    // Constructor
    public Vnpay(int responseCode, String responseData) {
        this.responseCode = responseCode;
        this.responseData = responseData;
    }

    // Getters và Setters
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
}
