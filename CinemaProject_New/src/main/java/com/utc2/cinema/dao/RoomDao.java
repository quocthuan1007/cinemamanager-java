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
    public boolean saveOrUpdateRoom(Room room) {
        try {
            Connection conn = Database.getConnection();
            String checkSql = "SELECT id FROM Room WHERE name = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, room.getName());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Phòng đã tồn tại → Cập nhật
                int existingId = rs.getInt("id");
                String updateSql = "UPDATE Room SET RowNumber = ?, ColNumber = ?, RoomStatus = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, room.getNumRows());
                updateStmt.setInt(2, room.getNumCols());
                updateStmt.setString(3, room.getRoomStatus());
                updateStmt.setInt(4, existingId);
                return updateStmt.executeUpdate() > 0;
            } else {
                // Phòng mới → Thêm mới
                String insertSql = "INSERT INTO Room (Name, RowNumber, ColNumber, RoomStatus) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, room.getName());
                insertStmt.setInt(2, room.getNumRows());
                insertStmt.setInt(3, room.getNumCols());
                insertStmt.setString(4, room.getRoomStatus());
                return insertStmt.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRoomById(int roomId) {
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM Room WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<String> getAllRoomNames() {
        List<String> roomNames = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT Name FROM Room"; // Lấy tất cả tên phòng
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                roomNames.add(rs.getString("Name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomNames; // Trả về danh sách tên phòng
    }
    public Room getRoomByName(String name) {
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT * FROM Room WHERE Name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Room(
                        rs.getInt("id"),
                        rs.getInt("RowNumber"),
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
    public void addRoom(Room room) {
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO Room (Name, RowNumber, ColNumber, RoomStatus) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getNumRows());
            stmt.setInt(3, room.getNumCols());
            stmt.setString(4, room.getRoomStatus());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateRoom(Room room) {
        try {
            Connection conn = Database.getConnection();
            String sql = "UPDATE Room SET RowNumber = ?, ColNumber = ?, RoomStatus = ? WHERE Name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, room.getNumRows());
            stmt.setInt(2, room.getNumCols());
            stmt.setString(3, room.getRoomStatus());
            stmt.setString(4, room.getName());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteRoom(String name) {
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM Room WHERE Name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
