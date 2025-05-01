package com.utc2.cinema.service;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.model.entity.Film;

import java.util.List;

public class FilmService {
    private FilmDao filmDao = new FilmDao();

    public List<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }
    public static boolean deleteFilm(int filmId) {
        return FilmDao.deleteFilm(filmId);
    }
    public static boolean updateFilm(Film filmId){
        return FilmDao.updateFilm(filmId);
    }

}
