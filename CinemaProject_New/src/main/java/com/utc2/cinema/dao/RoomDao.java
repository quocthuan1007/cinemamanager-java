package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();  // Danh sách các phòng

        try {
            // Kết nối đến cơ sở dữ liệu
            Connection conn = Database.getConnection();

            // Câu lệnh SQL để lấy tất cả các phòng
            String sql = "SELECT * FROM Room";

            // Chuẩn bị câu lệnh SQL
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Thực thi câu lệnh SQL và lấy kết quả
            ResultSet rs = stmt.executeQuery();

            // Duyệt qua kết quả và thêm các phòng vào danh sách
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getInt("RowNumber"),  // Lấy số hàng từ cột RowNumber
                        rs.getInt("ColNumber"),  // Lấy số cột từ cột ColNumber
                        rs.getString("RoomStatus"), // Lấy trạng thái phòng
                        rs.getString("Name") // Lấy tên phòng
                );
                rooms.add(room);  // Thêm phòng vào danh sách
            }
        } catch (Exception e) {
            // In ra lỗi nếu có
            e.printStackTrace();
        }

        return rooms;  // Trả về danh sách các phòng
    }


}
