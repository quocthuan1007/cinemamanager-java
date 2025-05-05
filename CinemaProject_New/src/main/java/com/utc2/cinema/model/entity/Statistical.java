package com.utc2.cinema.model.entity;

public class Statistical {
    private String ngay;
    private double tienDoAn;
    private double tienDatVe;
    private double tongTien;

    public Statistical(String ngay, double tienDoAn, double tienDatVe, double tongTien) {
        this.ngay = ngay;
        this.tienDoAn = tienDoAn;
        this.tienDatVe = tienDatVe;
        this.tongTien = tongTien;
    }

    // Getter và setter
    public String getNgay() { return ngay; }
    public double getTienDoAn() { return tienDoAn; }
    public double getTienDatVe() { return tienDatVe; }
    public double getTongTien() { return tongTien; }
}

