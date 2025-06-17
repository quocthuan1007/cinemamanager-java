package com.utc2.cinema.model.entity;

import javafx.beans.property.*;

public class StatisticalFilm {
    private final StringProperty filmName;
    private final IntegerProperty showCount;
    private final IntegerProperty seatSold;
    private final DoubleProperty totalRevenue;

    public StatisticalFilm(String filmName, int showCount, int seatSold, double totalRevenue) {
        this.filmName = new SimpleStringProperty(filmName);
        this.showCount = new SimpleIntegerProperty(showCount);
        this.seatSold = new SimpleIntegerProperty(seatSold);
        this.totalRevenue = new SimpleDoubleProperty(totalRevenue);
    }

    // Property dùng cho TableView
    public StringProperty filmNameProperty() {
        return filmName;
    }

    public IntegerProperty showCountProperty() {
        return showCount;
    }

    public IntegerProperty seatSoldProperty() {
        return seatSold;
    }

    public DoubleProperty totalRevenueProperty() {
        return totalRevenue;
    }

    // Getter cho biểu đồ
    public String getFilmName() {
        return filmName.get();
    }

    public int getShowCount() {
        return showCount.get();
    }

    public int getSeatSold() {
        return seatSold.get();
    }

    public double getTotalRevenue() {
        return totalRevenue.get();
    }
}
