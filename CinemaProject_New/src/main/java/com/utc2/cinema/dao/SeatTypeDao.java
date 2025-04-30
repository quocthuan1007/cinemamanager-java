package com.utc2.cinema.dao;

import com.utc2.cinema.model.entity.SeatType;
import com.utc2.cinema.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SeatTypeDao {
    public static SeatType getSeatTypeById(int seatTypeId) {
        SeatType seatType = null;
        String sql = "SELECT * FROM SeatType WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatTypeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                seatType = new SeatType(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("cost")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return seatType;
    }
}
