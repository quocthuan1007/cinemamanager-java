/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utc2.cinema.model.entity;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class Film {
    private int id;
    private String name;
    private String country;
    private int length;
    private String director;
    private String actor;
    private int ageLimit;
    private String filmStatus;
    private String trailer;
    private String content;
    private String adPosterUrl;
    private String posterUrl;
    private Date releaseDate;
    private LocalDate Date;

    public Film(int id, String name, String country, int length, String director, String actor, int ageLimit,
                String filmStatus, String trailer, String content, String adPosterUrl, String posterUrl, Date releaseDate) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.length = length;
        this.director = director;
        this.actor = actor;
        this.ageLimit = ageLimit;
        this.filmStatus = filmStatus;
        this.trailer = trailer;
        this.content = content;
        this.adPosterUrl = adPosterUrl;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }
    public Film(int id, String name, String posterUrl) {
        this.id = id;
        this.name = name;
        this.posterUrl = posterUrl;
    }
    public Film(String name, String posterUrl) {
        this.name=name;
        this.posterUrl=posterUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getFilmStatus() {
        return filmStatus;
    }

    public void setFilmStatus(String filmStatus) {
        this.filmStatus = filmStatus;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdPosterUrl() {
        return adPosterUrl;
    }

    public void setAdPosterUrl(String adPosterUrl) {
        this.adPosterUrl = adPosterUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
