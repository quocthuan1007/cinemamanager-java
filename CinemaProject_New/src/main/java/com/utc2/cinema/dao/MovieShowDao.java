package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.MovieShow;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieShowDao {

    public List<MovieShow> getAllMovieShows() {
        List<MovieShow> list = new ArrayList<>();
        String sql = "SELECT * FROM MovieShow WHERE isDeleted = 0"; // Chỉ lấy những lịch chiếu chưa bị xóa

        // Sử dụng try-with-resources để tự động đóng tài nguyên
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Lặp qua các kết quả trả về từ truy vấn
            while (rs.next()) {
                MovieShow show = new MovieShow();
                show.setId(rs.getInt("id"));
                show.setStartTime(rs.getTimestamp("startTime").toLocalDateTime());
                show.setEndTime(rs.getTimestamp("endTime").toLocalDateTime());
                show.setFilmId(rs.getInt("filmId"));
                show.setRoomId(rs.getInt("roomId"));
                show.setDeleted(rs.getBoolean("isDeleted"));

                // Thêm đối tượng MovieShow vào danh sách
                list.add(show);
            }

        } catch (SQLException e) {
            // Ghi lại lỗi để dễ dàng theo dõi
            System.err.println("Error fetching movie shows: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
    public boolean deleteMovieShowById(int id) {
        String sql = "UPDATE MovieShow SET IsDeleted = 1 WHERE Id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa lịch chiếu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

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
            String query = """
            SELECT ms.* 
            FROM MovieShow ms
            JOIN Room r ON ms.RoomId = r.Id
            WHERE ms.StartTime >= ? 
              AND ms.StartTime < ? 
              AND ms.IsDeleted = 0 
              AND r.RoomStatus != 'Bảo trì'
        """;

            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setTimestamp(1, Timestamp.valueOf(date.atStartOfDay()));
                stmt.setTimestamp(2, Timestamp.valueOf(date.plusDays(1).atStartOfDay()));

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
    public void updateMovieShow(MovieShow show) {
        String sql = "UPDATE MovieShow SET StartTime = ?, EndTime = ?, FilmId = ?, RoomId = ? WHERE Id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(show.getStartTime()));
            ps.setTimestamp(2, Timestamp.valueOf(show.getEndTime()));
            ps.setInt(3, show.getFilmId());
            ps.setInt(4, show.getRoomId());
            ps.setInt(5, show.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        LocalDateTime now = LocalDateTime.now(); // Lấy thời điểm hiện tại

        String query = """
        SELECT ms.*
        FROM MovieShow ms
        JOIN Room r ON ms.RoomId = r.Id
        WHERE ms.FilmId = ? 
          AND ms.StartTime >= ? 
          AND ms.IsDeleted = 0
          AND r.RoomStatus != 'Bảo trì'
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, filmId);
            pstmt.setTimestamp(2, Timestamp.valueOf(now)); // So sánh thời gian chính xác

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

    public void saveMovieShow(MovieShow movieShow) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO MovieShow (startTime, endTime, filmId, roomId, isDeleted) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setTimestamp(1, Timestamp.valueOf(movieShow.getStartTime()));
                stmt.setTimestamp(2, Timestamp.valueOf(movieShow.getEndTime()));
                stmt.setInt(3, movieShow.getFilmId());
                stmt.setInt(4, movieShow.getRoomId());
                stmt.setBoolean(5, movieShow.isDeleted());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            movieShow.setId(generatedKeys.getInt(1)); // Lấy ID vừa tạo
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean hasAnyRelatedData(int movieShowId) {
        String query = "SELECT 1 FROM Reservation WHERE ShowId = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieShowId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Trả về true nếu có ít nhất 1 bản ghi
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
