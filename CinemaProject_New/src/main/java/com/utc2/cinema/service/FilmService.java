package com.utc2.cinema.service;

import com.utc2.cinema.model.entity.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmService {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USER = "root";
    private static final String PASSWORD = "071205";

    // Hàm lấy tất cả các phim từ cơ sở dữ liệu
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String query = "SELECT Name, Director, PosterUrl FROM Film"; // Câu truy vấn SQL

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Duyệt qua các dòng trong ResultSet
            while (rs.next()) {
                String name = rs.getString("Name");
                String director = rs.getString("Director");
                String posterUrl = rs.getString("PosterUrl");

                // Tạo đối tượng Film và thêm vào danh sách
                Film film = new Film(name, posterUrl);
                film.setDirector(director);

                films.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }
}


