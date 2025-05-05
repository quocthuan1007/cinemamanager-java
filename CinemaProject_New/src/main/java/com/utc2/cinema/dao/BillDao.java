package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Bill;
import com.utc2.cinema.model.entity.Invoice;

import java.sql.*;
import java.time.LocalDate;
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
    public List<Invoice> getInvoicesByUserAndDateRange(int userId) {
        List<Invoice> invoices = new ArrayList<>();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection(); // Giả sử bạn đã có phương thức getConnection() trong lớp Database
            String query = "SELECT " +
                    "b.DatePurchased, " +
                    "ms.StartTime AS SuatChieu, " +
                    "f.Name AS Phim, " +
                    "r.Name AS PhongChieu, " +
                    "COUNT(s.Id) AS SoGhe, " +
                    "SUM(rsv.Cost) AS GiaTriGhe, " +
                    "COALESCE((" +
                    "   SELECT SUM(fo.Count * fd.Cost) " +
                    "   FROM Food_Order fo " +
                    "   JOIN Food fd ON fo.FoodId = fd.Id " +
                    "   WHERE fo.BillId = b.Id), 0) AS GiaTriDoAn, " +
                    "SUM(rsv.Cost) + COALESCE((" +
                    "   SELECT SUM(fo.Count * fd.Cost) " +
                    "   FROM Food_Order fo " +
                    "   JOIN Food fd ON fo.FoodId = fd.Id " +
                    "   WHERE fo.BillId = b.Id), 0) AS GiaTri " +
                    "FROM " +
                    "Bill b " +
                    "JOIN Reservation rsv ON b.Id = rsv.BillId " +
                    "JOIN MovieShow ms ON rsv.ShowId = ms.Id " +
                    "JOIN Film f ON ms.FilmId = f.Id " +
                    "JOIN Room r ON ms.RoomId = r.Id " +
                    "JOIN Seats s ON rsv.SeatId = s.Id " +
                    "WHERE b.UserId = ? " +
                    "GROUP BY b.DatePurchased, ms.StartTime, f.Name, r.Name, b.Id";

            st = conn.prepareStatement(query);
            st.setInt(1, userId); // Gán giá trị userId vào câu lệnh SQL
            rs = st.executeQuery();

            // Duyệt qua kết quả trả về từ truy vấn và tạo đối tượng Invoice
            while (rs.next()) {
                // Tạo đối tượng Invoice từ dữ liệu trong ResultSet
                Invoice invoice = new Invoice(
                        rs.getDate("DatePurchased").toLocalDate(),  // Chuyển đổi Date thành LocalDate
                        rs.getString("SuatChieu"),                   // Thời gian chiếu phim
                        rs.getString("Phim"),                        // Tên phim
                        rs.getString("PhongChieu"),                  // Tên phòng chiếu
                        rs.getInt("SoGhe"),                          // Số ghế
                        rs.getDouble("GiaTriGhe"),                   // Giá trị ghế
                        rs.getDouble("GiaTriDoAn"),                  // Giá trị đồ ăn
                        rs.getDouble("GiaTri")                       // Tổng giá trị
                );
                invoices.add(invoice); // Thêm invoice vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối và giải phóng tài nguyên
            Database.closeConnection(conn); // Giả sử bạn đã có phương thức closeConnection trong lớp Database
            if (st != null) try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return invoices; // Trả về danh sách hóa đơn
    }




}
