package com.utc2.cinema.controller;

import com.utc2.cinema.dao.BillDao;
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
import java.util.List;

public class HistoryController {
    private MainMenuController mainMenu = new MainMenuController();

    @FXML private TableView<Invoice> invoiceTable;
    @FXML private TableColumn<Invoice, String> colNgay;
    @FXML private TableColumn<Invoice, String> colSuatChieu;
    @FXML private TableColumn<Invoice, String> colPhim;
    @FXML private TableColumn<Invoice, String> colPhong;
    @FXML private TableColumn<Invoice, String> colSoGhe;
    @FXML private TableColumn<Invoice, String> colGiaTri;

    @FXML private TextField dateRangeField;
    @FXML private Label greetingLabel;


    // Constructor nhận đối tượng MainMenuController
    public HistoryController(MainMenuController mainMenu) {
        // Gán các thành phần từ MainMenuController vào HistoryController
        this.invoiceTable = mainMenu.getInvoiceTable();
        this.colNgay = mainMenu.getColNgay();
        this.colSuatChieu = mainMenu.getColSuatChieu();
        this.colPhim = mainMenu.getColPhim();
        this.colPhong = mainMenu.getColPhong();
        this.colSoGhe = mainMenu.getColSoGhe();
        this.colGiaTri = mainMenu.getColGiaTri();
        this.dateRangeField = mainMenu.getDateRangeField();
        this.greetingLabel = mainMenu.getGreetingLabel();

        billDao = new BillDao();
    }

    private BillDao billDao;
    private ObservableList<Invoice> invoiceData = FXCollections.observableArrayList();

    // Hàm khởi tạo các cột trong bảng
    @FXML
    void initialize() {
        billDao = new BillDao();
        User Info = UserService.getUser(UserSession.getInstance().getUserId());
        if (Info != null) {
            int userID = Info.getId();
            // Tự động hiển thị tất cả hóa đơn khi giao diện được khởi tạo
            List<Invoice> invoices = billDao.getBillsByUserAndDateRange(userID); // Lấy tất cả hóa đơn

            // Sử dụng SimpleStringProperty để chuyển đổi String thành ObservableValue<String>
            colNgay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatePurchased().toString()));
            colSuatChieu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovieShow()));
            colPhim.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovieTitle()));
            colPhong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoomName()));
            colSoGhe.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSeatCount())));
            colGiaTri.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTotalCost())));

            invoiceData.setAll(invoices);
            invoiceTable.setItems(invoiceData);
        }
    }
}
