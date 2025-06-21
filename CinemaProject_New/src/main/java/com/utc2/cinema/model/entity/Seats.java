/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utc2.cinema.model.entity;
/**
 *
 * @author ADMIN
 */
public class Seats {
    private int id;
    private String position;
    private int roomId;
    private int seatTypeId;
    private String seatStatus;

    public Seats(int id, String position, int roomId, int seatTypeId) {
        this.id = id;
        this.position = position;
        this.roomId = roomId;
        this.seatTypeId = seatTypeId;
    }
    public Seats( String position, int roomId, int seatTypeId) {
        this.id = id;
        this.position = position;
        this.roomId = roomId;
        this.seatTypeId = seatTypeId;
    }
    public Seats(int id, String position, int roomId, int seatTypeId, String seatStatus) {
        this.id = id;
        this.position = position;
        this.roomId = roomId;
        this.seatTypeId = seatTypeId;
        this.seatStatus = seatStatus;
    }

    public Seats() {
        // constructor mặc định
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getSeatTypeId() {
        return seatTypeId;
    }

    public void setSeatTypeId(int seatTypeId) {
        this.seatTypeId = seatTypeId;
    }

    public String getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }
}
