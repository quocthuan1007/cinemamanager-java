package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDao {

    // Lấy tất cả món ăn từ cơ sở dữ liệu
    public static List<Food> getAllFoods() {
        List<Food> foods = new ArrayList<>();
        String query = "SELECT id, name, description, cost FROM Food";

        Connection conn = Database.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Lấy thông tin từ ResultSet
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int cost = rs.getInt("cost");

                // Tạo đối tượng Food
                Food food = new Food(id, name, description, cost);

                // Thêm món ăn vào danh sách
                foods.add(food);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }

        return foods;
    }

    // Lấy món ăn theo ID
    public static Food getFoodById(int foodId) {
        if (foodId <= 0) {
            System.out.println("ID món ăn không hợp lệ.");
            return null; // Nếu foodId không hợp lệ (<= 0), trả về null.
        }

        Food food = null;
        String query = "SELECT id, name, description, cost FROM Food WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, foodId); // Thiết lập tham số ID món ăn vào câu truy vấn.

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Nếu có dữ liệu trả về, tạo đối tượng Food từ ResultSet.
                    food = new Food(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("cost"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có.
            System.out.println("Lỗi khi lấy thông tin món ăn với ID: " + foodId); // Lỗi nếu không thể truy xuất món ăn.
        }

        return food; // Trả về đối tượng Food nếu tìm thấy, hoặc null nếu không tìm thấy.
    }

    public static Food insertFood(Food food) {
        String query = "INSERT INTO Food (name, description, cost) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, food.getName());
            pstmt.setString(2, food.getDescription());
            pstmt.setFloat(3, food.getCost());

            int result = pstmt.executeUpdate();

            if (result > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        food.setId(generatedId);  // Cập nhật ID
                        System.out.println("Thêm món ăn thành công với ID: " + generatedId);
                        return food; // Trả về đối tượng Food đã có ID
                    }
                }
            } else {
                System.out.println("Không có bản ghi nào được thêm.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi thêm món ăn.");
        }

        return null; // Trả về null nếu thất bại
    }



    // Cập nhật thông tin món ăn
    public boolean updateFood(Food food) {
        String query = "UPDATE Food SET name = ?, description = ?, cost = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, food.getName());
            pstmt.setString(2, food.getDescription());
            pstmt.setFloat(3, food.getCost());
            pstmt.setInt(4, food.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Thông tin món ăn đã được cập nhật.");
                return true;  // Trả về true nếu cập nhật thành công
            } else {
                System.out.println("Không tìm thấy món ăn với ID đã cho.");
                return false;  // Trả về false nếu không có món ăn nào được cập nhật
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi cập nhật món ăn.");
            return false;  // Trả về false nếu có lỗi xảy ra
        }
    }


    // Xóa món ăn theo ID
    public boolean deleteFood(int foodId) {
        String query = "DELETE FROM Food WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, foodId);
            int a = pstmt.executeUpdate();

            System.out.println("Món ăn đã được xóa thành công.");
            return a > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi xóa món ăn.");
            return false;
        }
    }
}
