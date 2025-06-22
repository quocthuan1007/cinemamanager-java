package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {

    // Lấy tất cả Reservation
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservation";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Reservation reservation = extractReservationFromResultSet(rs);
                reservations.add(reservation);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách Reservation: " + e.getMessage());
            e.printStackTrace();
        }

        return reservations;
    }

    // Lấy Reservation theo Id
    public Reservation getReservationById(int id) {
        Reservation reservation = null;
        String query = "SELECT * FROM Reservation WHERE Id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    reservation = extractReservationFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy Reservation theo Id: " + e.getMessage());
            e.printStackTrace();
        }

        return reservation;
    }

    // Thêm mới Reservation
    public static boolean insertReservation(Reservation reservation) {
        String query = "INSERT INTO Reservation (BillId, SeatId, ShowId, Cost, SeatTypeName) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, reservation.getBillId());
            pstmt.setInt(2, reservation.getSeatId());
            pstmt.setInt(3, reservation.getShowId());
            pstmt.setInt(4, reservation.getCost());
            pstmt.setString(5, reservation.getSeatTypeName());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm Reservation: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật Reservation
    public boolean updateReservation(Reservation reservation) {
        String query = "UPDATE Reservation SET BillId = ?, SeatId = ?, ShowId = ?, Cost = ?, SeatTypeName = ? WHERE Id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, reservation.getBillId());
            pstmt.setInt(2, reservation.getSeatId());
            pstmt.setInt(3, reservation.getShowId());
            pstmt.setInt(4, reservation.getCost());
            pstmt.setString(5, reservation.getSeatTypeName());
            pstmt.setInt(6, reservation.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật Reservation: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Xóa Reservation
    public boolean deleteReservation(int id) {
        String query = "DELETE FROM Reservation WHERE Id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa Reservation: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Hàm hỗ trợ: đọc ResultSet -> Reservation object
    private Reservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("Id");
        int billId = rs.getInt("BillId");
        int seatId = rs.getInt("SeatId");
        int showId = rs.getInt("ShowId");
        int cost = rs.getInt("Cost");
        String seatTypeName = rs.getString("SeatTypeName");

        return new Reservation(id, billId, seatId, showId, cost, seatTypeName);
    }
    public static List<String> getReservedSeatsByShowId(int showId) {
        List<String> reservedSeats = new ArrayList<>();
        String query = "SELECT s.Position FROM Reservation r " +
                "JOIN Seats s ON r.SeatId = s.Id " +
                "WHERE r.ShowId = ?";  // Không cần lọc IsDeleted

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, showId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String seatPosition = rs.getString("Position");  // Lấy tên ghế (ví dụ A1, B3...)
                    reservedSeats.add(seatPosition);  // Thêm tên ghế vào danh sách
                }

            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách ghế đã đặt: " + e.getMessage());
            e.printStackTrace();
        }

        return reservedSeats;
    }
    public List<String> getSeatsByShowIdAndStatus(int showId, String status) {
        List<String> seats = new ArrayList<>();
        String sql = """
        SELECT s.position FROM Reservation r
        JOIN Seats s ON r.SeatId = s.Id
        WHERE r.ShowId = ? AND r.SeatStatus = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showId);
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                seats.add(rs.getString("position"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return seats;
    }
    public boolean insertChoosingReservation(int showId, int seatId, int cost, String seatTypeName) {
        String sql = """
        INSERT INTO Reservation (SeatId, ShowId, Cost, SeatTypeName, SeatStatus)
        VALUES (?, ?, ?, ?, 'CHOOSING')
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatId);
            stmt.setInt(2, showId);
            stmt.setInt(3, cost);
            stmt.setString(4, seatTypeName);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteChoosingReservation(int seatId, int showId) {
        String sql = "DELETE FROM Reservation WHERE SeatId = ? AND ShowId = ? AND SeatStatus = 'CHOOSING'";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatId);
            stmt.setInt(2, showId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public void deleteExpiredChoosingReservations(int showId, int expireMinutes) {
        String sql = "DELETE FROM Reservation " +
                "WHERE ShowId = ? AND SeatStatus = 'CHOOSING' AND TIMESTAMPDIFF(MINUTE, TimeCreated, NOW()) >= ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showId);
            stmt.setInt(2, expireMinutes);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
