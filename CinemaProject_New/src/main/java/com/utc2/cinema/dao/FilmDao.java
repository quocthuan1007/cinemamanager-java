package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilmDao {

    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String query = "SELECT Name, Country, Length, Director, Actor, AgeLimit, FilmStatus, Content, Trailer, AdPosterUrl, PosterUrl, ReleaseDate FROM Film";

        Connection conn = Database.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
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

                // Tạo đối tượng Film với đầy đủ thông tin
                Film film = new Film(name, posterUrl);
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

}
