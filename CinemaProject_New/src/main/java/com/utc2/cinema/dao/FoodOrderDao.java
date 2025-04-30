package com.utc2.cinema.dao;

import com.utc2.cinema.model.entity.Food;
import com.utc2.cinema.model.entity.FoodOrder;
import com.utc2.cinema.config.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodOrderDao {

    // Thêm một món ăn vào hóa đơn
    public static void insertFoodOrder(FoodOrder foodOrder) {
        String query = "INSERT INTO Food_Order (FoodId, BillId, Count) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, foodOrder.getFoodId());
            pstmt.setInt(2, foodOrder.getBillId());
            pstmt.setInt(3, foodOrder.getCount());
            pstmt.executeUpdate();

            System.out.println("Món ăn đã được thêm vào hóa đơn.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi thêm món ăn vào hóa đơn.");
        }
    }

    // Lấy danh sách món ăn trong một hóa đơn
    public static List<FoodOrder> getFoodOrdersByBillId(int billId) {
        String query = "SELECT * FROM Food_Order WHERE BillId = ?";
        List<FoodOrder> foodOrders = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Id");
                int foodId = rs.getInt("FoodId");
                int count = rs.getInt("Count");

                Food food = FoodDao.getFoodById(foodId); // Giả sử bạn đã có lớp FoodDao để lấy thông tin món ăn
                foodOrders.add(new FoodOrder(id, foodId, billId, count, food));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodOrders;
    }

    // Cập nhật số lượng món ăn trong hóa đơn
    public static boolean updateFoodOrderCount(int foodOrderId, int newCount) {
        String query = "UPDATE Food_Order SET Count = ? WHERE Id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, newCount);
            stmt.setInt(2, foodOrderId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa món ăn khỏi hóa đơn
    public static boolean deleteFoodOrder(int foodOrderId) {
        String query = "DELETE FROM Food_Order WHERE Id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, foodOrderId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
