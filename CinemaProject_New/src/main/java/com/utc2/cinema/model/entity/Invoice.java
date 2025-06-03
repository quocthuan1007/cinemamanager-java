package com.utc2.cinema.model.entity;

import java.time.LocalDate;
import java.util.List;

public class Invoice {
    private LocalDate datePurchased;
    private String movieShow;
    private String movieTitle;
    private String roomName;
    private int seatCount;
    private double seatCost;
    private double foodCost;
    private double totalCost;
    private int rating;  // hoặc Integer nếu có thể null
    private int id; // BillId
    private int userId;  // thêm
    private int filmId;  // thêm



    // ➕ Thuộc tính mới
    private List<String> seatList;
    private String foodCombo;

    // Constructor
    public Invoice(LocalDate datePurchased, String movieShow, String movieTitle, String roomName,
                   int seatCount, double seatCost, double foodCost, double totalCost,
                   List<String> seatList, String foodCombo) {
        this.datePurchased = datePurchased;
        this.movieShow = movieShow;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.seatCount = seatCount;
        this.seatCost = seatCost;
        this.foodCost = foodCost;
        this.totalCost = totalCost;
        this.seatList = seatList;
        this.foodCombo = foodCombo;
    }
    public Invoice(int id, LocalDate datePurchased, String movieShow, String movieTitle, String roomName,
                   int seatCount, double seatCost, double foodCost, double totalCost,
                   List<String> seatList, String foodCombo) {
        this.id = id;
        this.datePurchased = datePurchased;
        this.movieShow = movieShow;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.seatCount = seatCount;
        this.seatCost = seatCost;
        this.foodCost = foodCost;
        this.totalCost = totalCost;
        this.seatList = seatList;
        this.foodCombo = foodCombo;
    }


    public Invoice(LocalDate datePurchased, String movieShow, String movieTitle,
                   String roomName, int seatCount, double seatCost, double foodCost,
                   double totalCost, List<String> seatList, String foodCombo, int filmId) {
        this.datePurchased = datePurchased;
        this.movieShow = movieShow;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.seatCount = seatCount;
        this.seatCost = seatCost;
        this.foodCost = foodCost;
        this.totalCost = totalCost;
        this.seatList = seatList;
        this.foodCombo = foodCombo;
        this.filmId = filmId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    // ➕ Getter/Setter cho seatList
    public List<String> getSeatList() {
        return seatList;
    }
    public String getSeatNumbers() {
        if (seatList == null || seatList.isEmpty()) {
            return "";
        }
        return String.join(", ", seatList);
    }


    public void setSeatList(List<String> seatList) {
        this.seatList = seatList;
    }

    // ➕ Getter/Setter cho foodCombo
    public String getFoodCombo() {
        return foodCombo;
    }

    public void setFoodCombo(String foodCombo) {
        this.foodCombo = foodCombo;
    }
    // Getter cho rating
    public int getRating() {
        return rating;
    }

    // (Nếu cần) Setter cho rating
    public void setRating(int rating) {
        this.rating = rating;
    }
}
