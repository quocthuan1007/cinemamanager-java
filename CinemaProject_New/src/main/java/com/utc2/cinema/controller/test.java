package com.utc2.cinema.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.utc2.cinema.dao.BillDao;
import com.utc2.cinema.model.entity.Invoice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class test {

    @FXML
    private TableView<Invoice> invoiceTable;
    @FXML
    private TableColumn<Invoice, String> colNgay;
    @FXML
    private TableColumn<Invoice, String> colSuatChieu;
    @FXML
    private TableColumn<Invoice, String> colPhim;
    @FXML
    private TableColumn<Invoice, String> colPhong;
    @FXML
    private TableColumn<Invoice, String> colSoGhe;
    @FXML
    private TableColumn<Invoice, String> colGiaTri;

    @FXML
    private TextField dateRangeField;
    @FXML
    private Label greetingLabel;

    private BillDao billDao;

    public test() {
        billDao = new BillDao();
    }

    // Phương thức xử lý sự kiện lọc hóa đơn theo khoảng thời gian
    @FXML
    private void handleFilterInvoices(ActionEvent event) {
        String dateRange = dateRangeField.getText();
        if (dateRange != null && !dateRange.trim().isEmpty()) {
            try {
                // Tách khoảng ngày
                String[] dates = dateRange.split(" - ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate startDate = LocalDate.parse(dates[0], formatter);
                LocalDate endDate = LocalDate.parse(dates[1], formatter);

                // Lọc hóa đơn theo khoảng thời gian
                List<Invoice> invoices = billDao.getBillsByUserAndDateRange(1); // Cập nhật phương thức gọi từ Bill sang Invoice
                populateInvoiceTable(invoices);

            } catch (Exception e) {
                // Xử lý nếu định dạng ngày sai
                greetingLabel.setText("Định dạng ngày sai. Vui lòng nhập lại.");
            }
        } else {
            // Nếu không có khoảng thời gian, lấy tất cả hóa đơn
            List<Invoice> invoices = billDao.getBillsByUserAndDateRange(1); // Lấy tất cả hóa đơn
            populateInvoiceTable(invoices);
        }
    }

    // Phương thức để điền dữ liệu vào bảng hóa đơn
    private void populateInvoiceTable(List<Invoice> invoices) {
        ObservableList<Invoice> invoiceData = FXCollections.observableArrayList(invoices);
        invoiceTable.setItems(invoiceData);
    }

    // Hàm khởi tạo các cột trong bảng
    @FXML
    private void initialize() {
        // Tự động hiển thị tất cả hóa đơn khi giao diện được khởi tạo
        List<Invoice> invoices = billDao.getBillsByUserAndDateRange(1); // Lấy tất cả hóa đơn
        populateInvoiceTable(invoices);

        // Sử dụng SimpleStringProperty để chuyển đổi String thành ObservableValue<String>
        colNgay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatePurchased().toString()));
        colSuatChieu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovieShow()));
        colPhim.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovieTitle()));
        colPhong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoomName()));
        colSoGhe.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSeatCount())));
        colGiaTri.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTotalCost())));
    }

}
