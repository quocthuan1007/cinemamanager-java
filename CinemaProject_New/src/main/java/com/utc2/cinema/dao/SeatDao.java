package com.utc2.cinema.dao;

import com.utc2.cinema.model.entity.Seats;
import com.utc2.cinema.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDao {
    public static Seats getSeatByPositionAndRoom(String position, int roomId) {
        Seats seat = null;
        String sql = "SELECT * FROM Seats WHERE position = ? AND roomId = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, position);
            stmt.setInt(2, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                seat = new Seats(
                        rs.getInt("id"),
                        rs.getString("position"),
                        rs.getInt("roomId"),
                        rs.getInt("seatTypeId")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return seat;
    }
    public static void addSeat(Seats seat) {
        String sql = "INSERT INTO Seats (position, roomId, seatTypeId) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, seat.getPosition());
            stmt.setInt(2, seat.getRoomId());
            stmt.setInt(3, seat.getSeatTypeId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addSeatsBatch(List<Seats> seats) {
        String sql = "INSERT INTO Seats (position, roomId, seatTypeId) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Seats seat : seats) {
                stmt.setString(1, seat.getPosition());
                stmt.setInt(2, seat.getRoomId());
                stmt.setInt(3, seat.getSeatTypeId());
                stmt.addBatch(); // Thêm vào batch
            }

            stmt.executeBatch(); // Thực thi batch
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Seats> getSeatsByRoom(int roomId) {
        List<Seats> seatsList = new ArrayList<>();
        String sql = "SELECT * FROM Seats WHERE roomId = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Seats seat = new Seats();
                seat.setId(rs.getInt("id"));
                seat.setPosition(rs.getString("position"));
                seat.setRoomId(rs.getInt("roomId"));
                seat.setSeatTypeId(rs.getInt("seatTypeId"));

                seatsList.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seatsList;
    }

}
