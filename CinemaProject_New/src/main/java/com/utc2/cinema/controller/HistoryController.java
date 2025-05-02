package com.utc2.cinema.controller;

import com.utc2.cinema.dao.BillDao;
import com.utc2.cinema.model.entity.Bill;
import com.utc2.cinema.model.entity.Invoice;
import com.utc2.cinema.model.entity.User;
import com.utc2.cinema.model.entity.UserSession;
import com.utc2.cinema.service.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class HistoryController {
    private MainMenuController mainMenu = new MainMenuController();
    private TableView<Bill> billTable;

    private TableColumn<Bill, String> colNgay;
    private TableColumn<Bill, String> colTrangThai;
    private TextField dateRangeField;
    private Label greetingLabel;

    // Constructor nhận đối tượng MainMenuController
    public HistoryController(MainMenuController mainMenu) {
        // Gán các thành phần từ MainMenuController vào HistoryController
        this.billTable = mainMenu.getInvoiceTable();
        this.colNgay = mainMenu.getColNgay();
        this.colTrangThai = mainMenu.getColTrangThai();

        this.dateRangeField = mainMenu.getDateRangeField();
        this.greetingLabel = mainMenu.getGreetingLabel();
    }

    public ObservableList<Bill> getBillData() {
        return billData;
    }

    private ObservableList<Bill> billData = FXCollections.observableArrayList();

    public void init() {
        colNgay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatePurchased().toString()));
        colTrangThai.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBillStatus()));
        billData.setAll(listBill());
        billTable.setItems(billData);
    }

    ObservableList<Bill> listBill() {
        User Info = UserService.getUser(UserSession.getInstance().getUserId());
        ObservableList<Bill> listBill = FXCollections.observableArrayList();
        if(Info != null){
            List<Bill> list = BillDao.getBillsByUserId(Info.getId());
            for (Bill bill : list) {
                listBill.add(bill);
            }
        }
        return listBill;
    }
}
