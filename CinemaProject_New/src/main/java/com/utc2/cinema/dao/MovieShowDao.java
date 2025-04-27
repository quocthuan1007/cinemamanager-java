package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.MovieShow;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieShowDao {

    // Lấy danh sách các buổi chiếu của phim theo FilmId và ngày
    public List<MovieShow> getShowByFilmIdAndDate(int filmId, LocalDate date) {
        List<MovieShow> movieShows = new ArrayList<>();
        String query = "SELECT Id, StartTime, EndTime, FilmId, RoomId, IsDeleted " +
                "FROM MovieShow " +
                "WHERE FilmId = ? AND DATE(StartTime) = ? AND IsDeleted = 0 " +
                "ORDER BY StartTime";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, filmId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp tsStart = rs.getTimestamp("StartTime");
                    Timestamp tsEnd = rs.getTimestamp("EndTime");

                    if (tsStart != null && tsEnd != null) {
                        LocalDateTime start = tsStart.toLocalDateTime();
                        LocalDateTime end = tsEnd.toLocalDateTime();

                        MovieShow movieShow = new MovieShow(
                                rs.getInt("Id"),
                                start,
                                end,
                                rs.getInt("FilmId"),
                                rs.getInt("RoomId"),
                                rs.getBoolean("IsDeleted")
                        );
                        movieShows.add(movieShow);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn lịch chiếu: " + e.getMessage());
            e.printStackTrace();
        }

        return movieShows;
    }
    public List<MovieShow> getShowsByDate(LocalDate date) {
        List<MovieShow> movieShows = new ArrayList<>();
        try {
            // Sử dụng Database.getConnection() thay vì connection
            String query = "SELECT * FROM MovieShow WHERE DATE(StartTime) = ? AND IsDeleted = 0"; // Có thể cần thêm điều kiện IsDeleted = 0 nếu bạn không muốn lấy những bản ghi đã bị xóa
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setDate(1, java.sql.Date.valueOf(date)); // Chuyển LocalDate sang java.sql.Date
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    MovieShow movieShow = new MovieShow();
                    movieShow.setId(rs.getInt("Id"));
                    movieShow.setFilmId(rs.getInt("FilmId"));
                    movieShow.setStartTime(rs.getTimestamp("StartTime").toLocalDateTime());
                    movieShow.setEndTime(rs.getTimestamp("EndTime").toLocalDateTime());
                    movieShow.setRoomId(rs.getInt("RoomId"));
                    movieShows.add(movieShow);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movieShows;
    }



    // Lấy danh sách các buổi chiếu của phim theo FilmId
    public List<MovieShow> getShowsByFilmId(int filmId) {
        List<MovieShow> movieShows = new ArrayList<>();
        String sql = "SELECT Id, StartTime, EndTime, FilmId, RoomId, IsDeleted " +
                "FROM MovieShow WHERE FilmId = ? AND IsDeleted = 0";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filmId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Timestamp startTime = rs.getTimestamp("StartTime");
                Timestamp endTime = rs.getTimestamp("EndTime");
                int roomId = rs.getInt("RoomId");
                boolean isDeleted = rs.getBoolean("IsDeleted");

                MovieShow movieShow = new MovieShow(
                        rs.getInt("Id"),
                        startTime.toLocalDateTime(),
                        endTime.toLocalDateTime(),
                        filmId,
                        roomId,
                        isDeleted
                );
                movieShows.add(movieShow);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn lịch chiếu: " + e.getMessage());
            e.printStackTrace();
        }
        return movieShows;
    }
    // Lấy danh sách các suất chiếu của phim từ ngày hôm nay trở đi
    public List<MovieShow> getMovieShowsFromTodayOnward(int filmId) {
        List<MovieShow> movieShows = new ArrayList<>();
        LocalDate today = LocalDate.now(); // Lấy ngày hôm nay

        String query = "SELECT * FROM MovieShow WHERE FilmId = ? AND DATE(StartTime) >= ? AND IsDeleted = 0"; // Điều kiện StartTime >= hôm nay

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, filmId);
            pstmt.setDate(2, java.sql.Date.valueOf(today)); // Chuyển LocalDate sang java.sql.Date

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MovieShow movieShow = new MovieShow();
                    movieShow.setId(rs.getInt("Id"));
                    movieShow.setFilmId(rs.getInt("FilmId"));
                    movieShow.setStartTime(rs.getTimestamp("StartTime").toLocalDateTime());
                    movieShow.setEndTime(rs.getTimestamp("EndTime").toLocalDateTime());
                    movieShow.setRoomId(rs.getInt("RoomId"));
                    movieShows.add(movieShow);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieShows;
    }



}
