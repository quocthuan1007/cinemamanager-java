package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RoomDao {

    public Room getRoomById(int roomId) {
        try {
            Connection conn = Database.getConnection(); // Kết nối của bạn nhé
            String sql = "SELECT * FROM Room WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Room(
                        rs.getInt("id"),
                        rs.getInt("RowNumber"),  // chú ý tên cột trong database
                        rs.getInt("ColNumber"),
                        rs.getString("RoomStatus"),
                        rs.getString("Name")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
