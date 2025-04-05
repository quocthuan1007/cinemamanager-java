/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utc2.cinema.model.entity;
/**
 *
 * @author ADMIN
 */
public class Reservation {
    private int id;
    private int billId;
    private int seatId;
    private int showId;
    private int cost;
    private String seatTypeName;

    public Reservation(int id, int billId, int seatId, int showId, int cost, String seatTypeName) {
        this.id = id;
        this.billId = billId;
        this.seatId = seatId;
        this.showId = showId;
        this.cost = cost;
        this.seatTypeName = seatTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getSeatTypeName() {
        return seatTypeName;
    }

    public void setSeatTypeName(String seatTypeName) {
        this.seatTypeName = seatTypeName;
    }
}
