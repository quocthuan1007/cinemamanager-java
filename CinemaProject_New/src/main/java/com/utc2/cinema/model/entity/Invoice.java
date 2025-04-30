package com.utc2.cinema.model.entity;

import java.time.LocalDate;

public class Invoice {
    private LocalDate datePurchased;
    private String movieShow;
    private String movieTitle;
    private String roomName;
    private int seatCount;
    private double seatCost;
    private double foodCost;
    private double totalCost;

    // Constructor
    public Invoice(LocalDate datePurchased, String movieShow, String movieTitle, String roomName,
                   int seatCount, double seatCost, double foodCost, double totalCost) {
        this.datePurchased = datePurchased;
        this.movieShow = movieShow;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.seatCount = seatCount;
        this.seatCost = seatCost;
        this.foodCost = foodCost;
        this.totalCost = totalCost;
    }

    // Getter and Setter methods
    public LocalDate getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(LocalDate datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getMovieShow() {
        return movieShow;
    }

    public void setMovieShow(String movieShow) {
        this.movieShow = movieShow;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public double getSeatCost() {
        return seatCost;
    }

    public void setSeatCost(double seatCost) {
        this.seatCost = seatCost;
    }

    public double getFoodCost() {
        return foodCost;
    }

    public void setFoodCost(double foodCost) {
        this.foodCost = foodCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
