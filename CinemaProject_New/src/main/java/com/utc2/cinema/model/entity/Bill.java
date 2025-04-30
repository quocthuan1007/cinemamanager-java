/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utc2.cinema.model.entity;

import java.util.Date;

/**
 *
 * @author lucas
 */
public class Bill {
    private int id;
    private int userId;
    private Date datePurchased;
    private String billStatus;

    public Bill(int id, int userId, Date datePurchased, String billStatus) {
        this.id = id;
        this.userId = userId;
        this.datePurchased = datePurchased;
        this.billStatus = billStatus;
    }
    public Bill( int userId, Date datePurchased, String billStatus) {

        this.userId = userId;
        this.datePurchased = datePurchased;
        this.billStatus = billStatus;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
