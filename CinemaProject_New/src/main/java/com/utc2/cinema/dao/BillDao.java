package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Bill;
import com.utc2.cinema.model.entity.FilmRating;
import com.utc2.cinema.model.entity.Invoice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.utc2.cinema.dao.FoodDao.getFoodComboByBillId;
import static com.utc2.cinema.dao.SeatDao.getSeatNamesByBillId;

public class BillDao {
    public static String getBillStatus(int billId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = Database.getConnection();
            String sql = "SELECT BillStatus FROM Bill WHERE Id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, billId);
            rs = st.executeQuery();

            if (rs.next()) {
                return rs.getString("BillStatus");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
            if (st != null) try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }
    public static boolean updateBillStatus(int billId, String newStatus) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = Database.getConnection();
            String sql = "UPDATE Bill SET BillStatus = ? WHERE Id = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, newStatus);
            st.setInt(2, billId);

            int rowsUpdated = st.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
            if (st != null) try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }
    public static boolean updateTotalPrice(int billId, double totalPrice) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = Database.getConnection();
            String sql = "UPDATE Bill SET TotalPrice = ? WHERE Id = ?";
            st = conn.prepareStatement(sql);
            st.setDouble(1, totalPrice);
            st.setInt(2, billId);

            int rowsUpdated = st.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
            if (st != null) try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

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
                double totalPrice = rs.getDouble("TotalPrice");

                return new Bill(billId, userId, datePurchased, billStatus,totalPrice);
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
                double totalPrice = rs.getDouble("TotalPrice");
                bills.add(new Bill(billId, userId, datePurchased, billStatus,totalPrice));
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
            conn = Database.getConnection();
            String query = "SELECT " +
                    "b.Id AS BillId, " +
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
                    "   WHERE fo.BillId = b.Id), 0) AS GiaTri, " +
                    "ms.FilmId " + // ✅ thêm dòng này
                    "FROM " +
                    "Bill b " +
                    "JOIN Reservation rsv ON b.Id = rsv.BillId " +
                    "JOIN MovieShow ms ON rsv.ShowId = ms.Id " +
                    "JOIN Film f ON ms.FilmId = f.Id " +
                    "JOIN Room r ON ms.RoomId = r.Id " +
                    "JOIN Seats s ON rsv.SeatId = s.Id " +
                    "WHERE b.UserId = ? " +
                    "GROUP BY b.Id, b.DatePurchased, ms.StartTime, f.Name, r.Name, ms.FilmId";

            st = conn.prepareStatement(query);
            st.setInt(1, userId);
            rs = st.executeQuery();

            while (rs.next()) {
                int billId = rs.getInt("BillId");
                int filmId = rs.getInt("FilmId");

                List<String> seatList = getSeatNamesByBillId(billId);
                String foodCombo = getFoodComboByBillId(billId);
                double totalUp = getBillById(billId).getTotalPrice();
                System.out.println(totalUp);
                Invoice invoice = new Invoice(
                        rs.getDate("DatePurchased").toLocalDate(),
                        rs.getString("SuatChieu"),
                        rs.getString("Phim"),
                        rs.getString("PhongChieu"),
                        rs.getInt("SoGhe"),
                        rs.getDouble("GiaTriGhe"),
                        rs.getDouble("GiaTriDoAn"),
                        totalUp,
                        seatList,
                        foodCombo,
                        filmId
                );

                invoices.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
            if (st != null) try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return invoices;
    }

    public boolean saveRating(int billId, int rating, String review) {
        try {
            // Lấy thông tin UserId và FilmId từ hóa đơn thông qua Reservation và MovieShow
            String query = "SELECT b.UserId, ms.FilmId " +
                    "FROM Bill b " +
                    "JOIN Reservation r ON b.Id = r.BillId " +
                    "JOIN MovieShow ms ON r.ShowId = ms.Id " +
                    "WHERE b.Id = ? " +
                    "LIMIT 1";  // tránh lấy quá nhiều bản ghi

            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("UserId");
                int filmId = rs.getInt("FilmId");

                FilmRating existing = FilmRatingDao.getRatingByUserAndFilm(userId, filmId);

                FilmRating filmRating = new FilmRating();
                filmRating.setUserId(userId);
                filmRating.setFilmId(filmId);
                filmRating.setRating(rating);
                filmRating.setReview(review);

                if (existing != null) {
                    return FilmRatingDao.updateRating(filmRating);
                } else {
                    return FilmRatingDao.insertRating(filmRating);
                }
            }

            Database.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }







}
