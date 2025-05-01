package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilmDao {

    // Lấy tất cả phim từ cơ sở dữ liệu
    public static List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String query = "SELECT Id, Name, Country, Length, Director, Actor, AgeLimit, FilmStatus, Content, Trailer, AdPosterUrl, PosterUrl, ReleaseDate FROM Film";

        Connection conn = Database.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Lấy thông tin từ ResultSet
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                String country = rs.getString("Country");
                int length = rs.getInt("Length");
                String director = rs.getString("Director");
                String actor = rs.getString("Actor");
                int ageLimit = rs.getInt("AgeLimit");
                String filmStatus = rs.getString("FilmStatus");
                String content = rs.getString("Content");
                String trailer = rs.getString("Trailer");
                String adPosterUrl = rs.getString("AdPosterUrl");
                String posterUrl = rs.getString("PosterUrl");
                Date releaseDate = rs.getDate("ReleaseDate");

                // Tạo đối tượng Film
                Film film = new Film(id, name, posterUrl);
                film.setDirector(director);
                film.setCountry(country);
                film.setLength(length);
                film.setActor(actor);
                film.setAgeLimit(ageLimit);
                film.setFilmStatus(filmStatus);
                film.setContent(content);
                film.setTrailer(trailer);
                film.setAdPosterUrl(adPosterUrl);
                film.setReleaseDate(releaseDate);

                // Thêm phim vào danh sách
                films.add(film);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }

        return films;
    }

    // Lấy phim theo ID
    public Film getFilmById(int filmId) {
        if (filmId <= 0) {
            System.out.println("ID phim không hợp lệ.");
            return null; // Nếu filmId không hợp lệ (<= 0), trả về null.
        }

        Film film = null;
        String query = "SELECT Id, Name, Country, Length, Director, Actor, AgeLimit, FilmStatus, Content, Trailer, AdPosterUrl, PosterUrl, ReleaseDate " +
                "FROM Film WHERE Id = ?"; // Truy vấn SQL để lấy thông tin phim theo filmId.

        try (Connection conn = Database.getConnection(); // Kết nối với cơ sở dữ liệu.
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, filmId); // Thiết lập tham số ID phim vào câu truy vấn.

            try (ResultSet rs = pstmt.executeQuery()) { // Thực thi truy vấn.
                if (rs.next()) {
                    // Nếu có dữ liệu trả về, tạo đối tượng Film từ ResultSet.
                    film = new Film(rs.getInt("Id"), rs.getString("Name"), rs.getString("PosterUrl"));
                    film.setDirector(rs.getString("Director"));
                    film.setCountry(rs.getString("Country"));
                    film.setLength(rs.getInt("Length"));
                    film.setActor(rs.getString("Actor"));
                    film.setAgeLimit(rs.getInt("AgeLimit"));
                    film.setFilmStatus(rs.getString("FilmStatus"));
                    film.setContent(rs.getString("Content"));
                    film.setTrailer(rs.getString("Trailer"));
                    film.setAdPosterUrl(rs.getString("AdPosterUrl"));
                    film.setReleaseDate(rs.getDate("ReleaseDate"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có.
            System.out.println("Lỗi khi lấy thông tin phim với ID: " + filmId); // Lỗi nếu không thể truy xuất phim.
        }

        return film; // Trả về đối tượng Film nếu tìm thấy, hoặc null nếu không tìm thấy.
    }

    // Lấy danh sách phim theo ngày chiếu (giả sử có trường DateTime trong MovieShow)
    public List<Film> getFilmsByReleaseDate(Date releaseDate) {
        List<Film> films = new ArrayList<>();
        String query = "SELECT Id, Name, Country, Length, Director, Actor, AgeLimit, FilmStatus, Content, Trailer, AdPosterUrl, PosterUrl, ReleaseDate FROM Film WHERE ReleaseDate = ?";

        Connection conn = Database.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(releaseDate.getTime())); // Thiết lập tham số ngày chiếu vào câu truy vấn.

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Lấy thông tin từ ResultSet và tạo đối tượng Film
                    Film film = new Film(rs.getInt("Id"), rs.getString("Name"), rs.getString("PosterUrl"));
                    film.setDirector(rs.getString("Director"));
                    film.setCountry(rs.getString("Country"));
                    film.setLength(rs.getInt("Length"));
                    film.setActor(rs.getString("Actor"));
                    film.setAgeLimit(rs.getInt("AgeLimit"));
                    film.setFilmStatus(rs.getString("FilmStatus"));
                    film.setContent(rs.getString("Content"));
                    film.setTrailer(rs.getString("Trailer"));
                    film.setAdPosterUrl(rs.getString("AdPosterUrl"));
                    film.setReleaseDate(rs.getDate("ReleaseDate"));

                    // Thêm vào danh sách phim
                    films.add(film);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }

        return films;
    }
    public static boolean insertFilm(Film film) {
        String sql = "INSERT INTO film (name, country, length, director, actor, ageLimit, filmStatus, trailer, content, adPosterUrl, posterUrl, releaseDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, film.getName());
            stmt.setString(2, film.getCountry());
            stmt.setInt(3, film.getLength());
            stmt.setString(4, film.getDirector());
            stmt.setString(5, film.getActor());
            stmt.setInt(6, film.getAgeLimit());
            stmt.setString(7, film.getFilmStatus());
            stmt.setString(8, film.getTrailer());
            stmt.setString(9, film.getContent());
            stmt.setString(10, film.getAdPosterUrl());
            stmt.setString(11, film.getPosterUrl());

            // Dùng Timestamp thay vì Date
            if (film.getReleaseDate() != null) {
                stmt.setTimestamp(12, new java.sql.Timestamp(film.getReleaseDate().getTime()));
            } else {
                stmt.setTimestamp(12, null);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean deleteFilm(int filmId) {
        String sql = "DELETE FROM Film WHERE Id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filmId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateFilm(Film film) {
        String sql = "UPDATE film SET name = ?, country = ?, length = ?, director = ?, actor = ?, ageLimit = ?, filmStatus = ?, trailer = ?, content = ?, adPosterUrl = ?, posterUrl = ?, releaseDate = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, film.getName());
            stmt.setString(2, film.getCountry());
            stmt.setInt(3, film.getLength());
            stmt.setString(4, film.getDirector());
            stmt.setString(5, film.getActor());
            stmt.setInt(6, film.getAgeLimit());
            stmt.setString(7, film.getFilmStatus());
            stmt.setString(8, film.getTrailer());
            stmt.setString(9, film.getContent());
            stmt.setString(10, film.getAdPosterUrl());
            stmt.setString(11, film.getPosterUrl());
            stmt.setTimestamp(12, new java.sql.Timestamp(film.getReleaseDate().getTime()));
            stmt.setInt(13, film.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

