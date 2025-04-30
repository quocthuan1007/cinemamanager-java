package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDao {

    public static boolean insertBill(Bill bill) {
        String sql = "INSERT INTO Bill (UserId, DatePurchased, BillStatus) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bill.getUserId());
            stmt.setTimestamp(2, new Timestamp(bill.getDatePurchased().getTime()));
            stmt.setString(3, bill.getBillStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bill.setId(generatedKeys.getInt(1)); // cập nhật id cho đối tượng Bill
                } else {
                    return false;
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // Cập nhật hóa đơn
    public static boolean updateBill(Bill bill) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = Database.getConnection();
            String sql = "UPDATE Bill SET BillStatus = ? WHERE Id = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, bill.getBillStatus());
            st.setInt(2, bill.getId());

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            }
            Database.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy thông tin hóa đơn theo id
    public static Bill getBillById(int id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = Database.getConnection();
            String sql = "SELECT * FROM Bill WHERE Id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                int billId = rs.getInt("Id");
                int userId = rs.getInt("UserId");
                java.sql.Date datePurchased = rs.getDate("DatePurchased");
                String billStatus = rs.getString("BillStatus");

                return new Bill(billId, userId, datePurchased, billStatus);
            }
            Database.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách hóa đơn của một người dùng
    public static List<Bill> getBillsByUserId(int userId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Bill> bills = new ArrayList<>();
        try {
            conn = Database.getConnection();
            String sql = "SELECT * FROM Bill WHERE UserId = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, userId);
            rs = st.executeQuery();

            while (rs.next()) {
                int billId = rs.getInt("Id");
                java.sql.Date datePurchased = rs.getDate("DatePurchased");
                String billStatus = rs.getString("BillStatus");

                bills.add(new Bill(billId, userId, datePurchased, billStatus));
            }
            Database.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bills;
    }

    // Xóa hóa đơn theo ID
    public static boolean deleteBill(int id) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = Database.getConnection();
            String sql = "DELETE FROM Bill WHERE Id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, id);

            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            }
            Database.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
