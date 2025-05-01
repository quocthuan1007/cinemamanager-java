/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utc2.cinema.model.entity;
import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
public class MovieShow {
    private int id;
    private LocalDateTime  startTime;
    private LocalDateTime endTime;
    private int filmId;
    private int roomId;
    private boolean isDeleted;



    public MovieShow(int id, LocalDateTime startTime, LocalDateTime endTime, int filmId, int roomId, boolean isDeleted) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.filmId = filmId;
        this.roomId = roomId;
        this.isDeleted = isDeleted;
    }
    public MovieShow( LocalDateTime startTime, LocalDateTime endTime, int filmId, int roomId, boolean isDeleted) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.filmId = filmId;
        this.roomId = roomId;
        this.isDeleted = isDeleted;
    }
    public MovieShow() {
        // Khởi tạo các giá trị mặc định cho các trường
        this.id = 0; // Hoặc có thể là -1 nếu bạn muốn chỉ định chưa có ID
        this.startTime = LocalDateTime.now(); // Mặc định là thời gian hiện tại
        this.endTime = LocalDateTime.now();   // Mặc định là thời gian hiện tại
        this.filmId = 0; // Giá trị mặc định cho filmId
        this.roomId = 0; // Giá trị mặc định cho roomId
        this.isDeleted = false; // Mặc định không bị xóa
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
