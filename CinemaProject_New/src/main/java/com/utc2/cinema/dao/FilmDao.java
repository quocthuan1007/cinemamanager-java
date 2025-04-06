package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FilmDao {

    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String query = "SELECT Name, Director, PosterUrl FROM Film";

        Connection conn = Database.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("Name");
                String director = rs.getString("Director");
                String posterUrl = rs.getString("PosterUrl");

                Film film = new Film(name, posterUrl);
                film.setDirector(director);
                films.add(film);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(conn);
        }

        return films;
    }
}
