package com.utc2.cinema.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USER = "root";
    private static final String PASSWORD = "071205";

    public static void main(String[] args) {
        // Kiểm tra kết nối cơ sở dữ liệu
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (conn != null) {
                System.out.println("Kết nối tới cơ sở dữ liệu thành công!");
            }
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }
    }
}
