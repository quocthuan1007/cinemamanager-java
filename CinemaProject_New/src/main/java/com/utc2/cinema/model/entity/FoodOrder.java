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
    private Food food;   // Món ăn liên quan
    private float totalPrice;

    // Constructor
    public FoodOrder(int id, int foodId, int billId, int count, Food food) {
        this.id = id;
        this.foodId = foodId;
        this.billId = billId;
        this.count = count;
        this.food = food;
    }

    // Getter và Setter
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    // Tính tổng tiền của món ăn trong giỏ hàng
    public float getTotalPrice() {
        return food.getCost() * (float) count;
    }


    // Cập nhật lại tổng tiền khi số lượng thay đổi
    public void updateTotalPrice() {
        // Phương thức này có thể được dùng khi bạn cần xử lý lại tổng tiền sau khi thay đổi số lượng
        // Mặc dù tổng tiền có thể được tính trực tiếp qua getTotalPrice(), bạn cũng có thể gọi updateTotalPrice nếu cần thêm thao tác
    }
    // Cập nhật lại tổng tiền của món ăn
    public void setTotalPrice(float cost, int count) {
        this.totalPrice = cost * (float)count;  // Tính tổng tiền = giá món ăn * số lượng
    }

}
