package com.utc2.cinema.dao;

import com.utc2.cinema.model.entity.Seats;
import com.utc2.cinema.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
