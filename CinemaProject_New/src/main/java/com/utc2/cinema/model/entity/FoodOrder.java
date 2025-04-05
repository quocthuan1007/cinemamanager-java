/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utc2.cinema.model.entity;
/**
 *
 * @author lucas
 */
public class FoodOrder {
    private int id;
    private int foodId;  // ID của món ăn
    private int billId;  // ID của hóa đơn
    private int count;   // Số lượng món ăn

    public FoodOrder(int id, int foodId, int billId, int count) {
        this.id = id;
        this.foodId = foodId;
        this.billId = billId;
        this.count = count;
    }

    public int getBillId() {
        return billId;
    }

    public int getCount() {
        return count;
    }

    public int getFoodId() {
        return foodId;
    }

    public int getId() {
        return id;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
