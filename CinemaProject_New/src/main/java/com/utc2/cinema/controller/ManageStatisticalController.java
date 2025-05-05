package com.utc2.cinema.controller;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.CustomAlert;
import com.utc2.cinema.model.entity.Statistical;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.sql.*;
import java.time.LocalDate;

public class ManageStatisticalController {

    private Pane thongKePane;
    private TableView<Statistical> tableThongKe;
    private TableColumn<Statistical, String> dateThongKe;
    private TableColumn<Statistical, Float> foodThongKe;
    private TableColumn<Statistical, Float> ticketThongKe;
    private TableColumn<Statistical, Float> sumThongKe;
    private DatePicker batDauThongKe;
    private DatePicker ketThucThongKe;
    public ManageStatisticalController(MainManagerController main)
    {
        thongKePane = main.getThongKePane();
        tableThongKe = main.getTableThongKe();
        dateThongKe = main.getDateThongKe();
        foodThongKe = main.getFoodThongKe();
        ticketThongKe = main.getTicketThongKe();
        sumThongKe = main.getSumThongKe();
        batDauThongKe = main.getBatDauThongKe();
        ketThucThongKe = main.getKetThucThongKe();
    }

    public void initialize() {
        dateThongKe.setCellValueFactory(new PropertyValueFactory<>("ngay"));
        foodThongKe.setCellValueFactory(new PropertyValueFactory<>("tienDoAn"));
        ticketThongKe.setCellValueFactory(new PropertyValueFactory<>("tienDatVe"));
        sumThongKe.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
    }
    public void onSearchThongKe(ActionEvent event) {
        System.out.println("Thống kê ...");
        LocalDate startDate = batDauThongKe.getValue();
        LocalDate endDate = ketThucThongKe.getValue();

        if (startDate == null || endDate == null) {
            CustomAlert.showError("","Có lỗi xảy ra!","Vui lòng chọn ngày bắt đầu và kết thúc!");
            return;
        }
        if (!startDate.isBefore(endDate))
        {
            CustomAlert.showError("", "Có lỗi xảy ra!", "Ngày bắt đầu phải trước ngày kết thúc!");
            return;
        }
        String sql = """
        SELECT 
            DATE(b.DatePurchased) AS Ngay,
            SUM(COALESCE(f.Cost * fo.Count, 0)) AS TienDoAn,
            SUM(COALESCE(rs.Cost, 0)) AS TienDatVe,
            SUM(COALESCE(f.Cost * fo.Count, 0) + COALESCE(rs.Cost, 0)) AS TongTien
        FROM 
            Bill b
        LEFT JOIN Food_Order fo ON fo.BillId = b.Id
        LEFT JOIN Food f ON f.Id = fo.FoodId
        LEFT JOIN Reservation rs ON rs.BillId = b.Id
        WHERE 
            b.DatePurchased BETWEEN ? AND ?
        GROUP BY 
            DATE(b.DatePurchased)
        ORDER BY 
            Ngay
    """;

        ObservableList<Statistical> data = FXCollections.observableArrayList();
        double totalAll = 0;
        double totalDoAn = 0;
        double totalVe = 0;

        try{
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, startDate.toString() + " 00:00:00");
            stmt.setString(2, endDate.toString() + " 23:59:59");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ngay = rs.getString("Ngay");
                double tienDoAn = rs.getDouble("TienDoAn");
                double tienDatVe = rs.getDouble("TienDatVe");
                double tongTien = rs.getDouble("TongTien");
                totalDoAn += tienDoAn;
                totalVe += tienDatVe;
                totalAll += tongTien;

                data.add(new Statistical(ngay, tienDoAn, tienDatVe, tongTien));
            }

            // Thêm dòng tổng cộng
            data.add(new Statistical("TỔNG CỘNG", totalDoAn, totalVe, totalAll));

            tableThongKe.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
