package com.utc2.cinema.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.utc2.cinema.dao.ThongKeDao;
import com.utc2.cinema.model.entity.CustomAlert;
import com.utc2.cinema.model.entity.StatisticalFilm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class test {

    @FXML private Pane thongKePane;
    @FXML private TableView<StatisticalFilm> tableThongKe;
    @FXML private TableColumn<StatisticalFilm, String> filmNameColumn;
    @FXML private TableColumn<StatisticalFilm, Integer> showCountColumn;
    @FXML private TableColumn<StatisticalFilm, Integer> seatSoldColumn;
    @FXML private TableColumn<StatisticalFilm, Double> totalRevenueColumn;
    @FXML private DatePicker batDauThongKe;
    @FXML private DatePicker ketThucThongKe;
    @FXML private TextField searchFilmField;
    @FXML private Label totalShowLabel;
    @FXML private Label totalSeatsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;

    public void initialize() {
        filmNameColumn.setCellValueFactory(cell -> cell.getValue().filmNameProperty());
        showCountColumn.setCellValueFactory(cell -> cell.getValue().showCountProperty().asObject());
        seatSoldColumn.setCellValueFactory(cell -> cell.getValue().seatSoldProperty().asObject());
        totalRevenueColumn.setCellValueFactory(cell -> cell.getValue().totalRevenueProperty().asObject());

        // ⏰ Mặc định chọn ngày từ 1 tháng trước đến hôm nay
        LocalDate now = LocalDate.now();
        batDauThongKe.setValue(now.minusMonths(1));
        ketThucThongKe.setValue(now);

        // 📊 Tải dữ liệu mặc định luôn khi khởi động
        loadData(null);
    }


    @FXML
    public void onSearchThongKe(ActionEvent event) {
        loadData(null);
    }

    @FXML
    public void onSearchFilmRevenue(ActionEvent event) {
        String keyword = searchFilmField.getText().trim();
        loadData(keyword);
    }

    private void loadData(String keyword) {
        LocalDate startDate = batDauThongKe.getValue();
        LocalDate endDate = ketThucThongKe.getValue();

        if (startDate == null || endDate == null) {
            CustomAlert.showError("Lỗi", "Vui lòng chọn ngày bắt đầu và kết thúc!");
            return;
        }
        if (!startDate.isBefore(endDate)) {
            CustomAlert.showError("Lỗi", "Ngày bắt đầu phải trước ngày kết thúc!");
            return;
        }

        try {
            ThongKeDao dao = new ThongKeDao();
            List<StatisticalFilm> list = dao.getFilmStatistics(startDate, endDate, keyword);
            ObservableList<StatisticalFilm> data = FXCollections.observableArrayList(list);

            int totalShows = data.stream().mapToInt(StatisticalFilm::getShowCount).sum();
            int totalSeats = data.stream().mapToInt(StatisticalFilm::getSeatSold).sum();
            double totalRevenue = data.stream().mapToDouble(StatisticalFilm::getTotalRevenue).sum();

            tableThongKe.setItems(data);
            totalShowLabel.setText(totalShows + " suất chiếu");
            totalSeatsLabel.setText(totalSeats + " vé đã bán");
            totalRevenueLabel.setText(String.format("%.0f VNĐ", totalRevenue));

            updateCharts(data);
        } catch (SQLException e) {
            e.printStackTrace();
            CustomAlert.showError("Lỗi", "Không thể truy xuất dữ liệu thống kê.");
        }
    }

    private void updateCharts(List<StatisticalFilm> data) {
        pieChart.getData().clear();
        barChart.getData().clear();

        XYChart.Series<String, Number> seatSeries = new XYChart.Series<>();
        seatSeries.setName("Vé bán");

        XYChart.Series<String, Number> showSeries = new XYChart.Series<>();
        showSeries.setName("Suất chiếu");

        int otherShows = 0, otherSeats = 0;
        int limit = Math.min(4, data.size());

        for (int i = 0; i < data.size(); i++) {
            StatisticalFilm sf = data.get(i);
            if (i < 4) {
                seatSeries.getData().add(new XYChart.Data<>(sf.getFilmName(), sf.getSeatSold()));
                showSeries.getData().add(new XYChart.Data<>(sf.getFilmName(), sf.getShowCount()));
                pieChart.getData().add(new PieChart.Data(sf.getFilmName(), sf.getSeatSold()));
            } else {
                otherSeats += sf.getSeatSold();
                otherShows += sf.getShowCount();
            }
        }

        if (data.size() > 4) {
            seatSeries.getData().add(new XYChart.Data<>("Khác", otherSeats));
            showSeries.getData().add(new XYChart.Data<>("Khác", otherShows));
            pieChart.getData().add(new PieChart.Data("Khác", otherSeats));
        }

        barChart.getData().addAll(seatSeries, showSeries);
    }

    @FXML
    public void onExportPdf(ActionEvent event) {
        LocalDate startDate = batDauThongKe.getValue();
        LocalDate endDate = ketThucThongKe.getValue();

        if (startDate == null || endDate == null) {
            CustomAlert.showError("Lỗi", "Vui lòng chọn ngày bắt đầu và kết thúc!");
            return;
        }

        List<StatisticalFilm> data = tableThongKe.getItems();
        if (data == null || data.isEmpty()) {
            CustomAlert.showWarning("Cảnh báo", "Không có dữ liệu để xuất.");
            return;
        }

        try {
            Document document = new Document(PageSize.A4);
            String filePath = "ThongKe_DoanhThu.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Paragraph title = new Paragraph("BÁO CÁO THỐNG KÊ DOANH THU PHIM\n",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            Paragraph dateInfo = new Paragraph("Từ ngày " + startDate + " đến ngày " + endDate + "\n\n");
            dateInfo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(dateInfo);

            PdfPTable table = new PdfPTable(4);
            table.setWidths(new int[]{3, 2, 2, 3});
            table.setWidthPercentage(100);

            table.addCell("Tên phim");
            table.addCell("Số suất chiếu");
            table.addCell("Số vé bán");
            table.addCell("Doanh thu");

            for (StatisticalFilm sf : data) {
                table.addCell(sf.getFilmName());
                table.addCell(String.valueOf(sf.getShowCount()));
                table.addCell(String.valueOf(sf.getSeatSold()));
                table.addCell(String.format("%.0f VNĐ", sf.getTotalRevenue()));
            }

            document.add(table);

            Paragraph totals = new Paragraph(String.format(
                    "\nTổng cộng: %d suất chiếu, %d vé bán, %.0f VNĐ doanh thu",
                    data.stream().mapToInt(StatisticalFilm::getShowCount).sum(),
                    data.stream().mapToInt(StatisticalFilm::getSeatSold).sum(),
                    data.stream().mapToDouble(StatisticalFilm::getTotalRevenue).sum()
            ));
            document.add(totals);

            document.close();

            CustomAlert.showSuccess("Thành công", "Xuất file PDF thành công:\n" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            CustomAlert.showError("Lỗi", "Không thể xuất file PDF.");
        }
    }
    @FXML
    public void onExportExcel(ActionEvent event) {
        List<StatisticalFilm> data = tableThongKe.getItems();
        if (data == null || data.isEmpty()) {
            CustomAlert.showWarning("Cảnh báo", "Không có dữ liệu để xuất.");
            return;
        }

        LocalDate startDate = batDauThongKe.getValue();
        LocalDate endDate = ketThucThongKe.getValue();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Thống kê phim");

            int rowIndex = 0;

            // Dòng thông tin ngày thống kê
            Row infoRow = sheet.createRow(rowIndex++);
            infoRow.createCell(0).setCellValue("Thống kê từ ngày " + startDate + " đến " + endDate);

            // Dòng trống
            rowIndex++;

            // Tiêu đề cột
            Row header = sheet.createRow(rowIndex++);
            header.createCell(0).setCellValue("Tên phim");
            header.createCell(1).setCellValue("Số suất chiếu");
            header.createCell(2).setCellValue("Số vé bán");
            header.createCell(3).setCellValue("Doanh thu (VNĐ)");

            // Dữ liệu từng dòng
            double totalRevenue = 0;
            for (StatisticalFilm sf : data) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(sf.getFilmName());
                row.createCell(1).setCellValue(sf.getShowCount());
                row.createCell(2).setCellValue(sf.getSeatSold());
                row.createCell(3).setCellValue(sf.getTotalRevenue());
                totalRevenue += sf.getTotalRevenue();
            }

            // Dòng trống
            rowIndex++;

            // Dòng tổng doanh thu
            Row totalRow = sheet.createRow(rowIndex);
            totalRow.createCell(2).setCellValue("Tổng doanh thu:");
            totalRow.createCell(3).setCellValue(String.format("%.0f", totalRevenue));

            // Tự động co giãn cột
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            // Lưu file
            String filePath = "ThongKe_DoanhThu.xlsx";
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }

            CustomAlert.showSuccess("Thành công", "Xuất file Excel thành công:\n" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            CustomAlert.showError("Lỗi", "Không thể xuất file Excel.");
        }
    }

}
