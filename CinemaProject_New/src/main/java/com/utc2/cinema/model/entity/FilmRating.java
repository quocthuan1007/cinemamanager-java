package com.utc2.cinema.model.entity;

import java.time.LocalDateTime;

public class FilmRating {
    private int id;
    private int userId;
    private int filmId;
    private int rating; // 1–5
    private String review;
    private LocalDateTime ratedAt;

    public FilmRating() {
    }

    public FilmRating(int id, int userId, int filmId, int rating, String review, LocalDateTime ratedAt) {
        this.id = id;
        this.userId = userId;
        this.filmId = filmId;
        this.rating = rating;
        this.review = review;
        this.ratedAt = ratedAt;
    }

    public FilmRating(int userId, int filmId, int rating, String review, LocalDateTime ratedAt) {
        this.userId = userId;
        this.filmId = filmId;
        this.rating = rating;
        this.review = review;
        this.ratedAt = ratedAt;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public LocalDateTime getRatedAt() {
        return ratedAt;
    }

    public void setRatedAt(LocalDateTime ratedAt) {
        this.ratedAt = ratedAt;
    }
}
