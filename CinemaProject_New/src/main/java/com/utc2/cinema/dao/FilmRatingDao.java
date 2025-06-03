package com.utc2.cinema.dao;

import com.utc2.cinema.model.entity.FilmRating;
import com.utc2.cinema.config.Database;

import java.sql.*;

public class FilmRatingDao {

    // Lấy đánh giá theo UserId và FilmId
    public static FilmRating getRatingByUserAndFilm(int userId, int filmId) {
        String sql = "SELECT * FROM FilmRating WHERE UserId = ? AND FilmId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    FilmRating rating = new FilmRating();
                    rating.setId(rs.getInt("Id"));
                    rating.setUserId(rs.getInt("UserId"));
                    rating.setFilmId(rs.getInt("FilmId"));
                    rating.setRating(rs.getInt("Rating"));
                    rating.setReview(rs.getString("Comment"));  // để ý cột Comment
                    rating.setRatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
                    return rating;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật đánh giá đã có
    public static boolean updateRating(FilmRating rating) {
        String sql = "UPDATE FilmRating SET Rating = ?, Comment = ?, CreatedAt = CURRENT_TIMESTAMP WHERE UserId = ? AND FilmId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rating.getRating());
            stmt.setString(2, rating.getReview());
            stmt.setInt(3, rating.getUserId());
            stmt.setInt(4, rating.getFilmId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm mới đánh giá
    public static boolean insertRating(FilmRating rating) {
        String sql = "INSERT INTO FilmRating (UserId, FilmId, Rating, Comment, CreatedAt) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rating.getUserId());
            stmt.setInt(2, rating.getFilmId());
            stmt.setInt(3, rating.getRating());
            stmt.setString(4, rating.getReview());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm saveRating kết hợp insert hoặc update tùy đánh giá đã tồn tại hay chưa
    public boolean saveRating(int userId, int filmId, int ratingValue, String comment) {
        FilmRating existingRating = getRatingByUserAndFilm(userId, filmId);
        if (existingRating != null) {
            // Cập nhật
            existingRating.setRating(ratingValue);
            existingRating.setReview(comment);
            return updateRating(existingRating);
        } else {
            // Thêm mới
            FilmRating newRating = new FilmRating();
            newRating.setUserId(userId);
            newRating.setFilmId(filmId);
            newRating.setRating(ratingValue);
            newRating.setReview(comment);
            return insertRating(newRating);
        }
    }

}
