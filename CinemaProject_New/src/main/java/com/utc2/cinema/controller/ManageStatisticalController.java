package com.utc2.cinema.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.utc2.cinema.config.Database;
import com.utc2.cinema.dao.ThongKeDao;
import com.utc2.cinema.model.entity.CustomAlert;

import com.utc2.cinema.model.entity.StatisticalFilm;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ManageStatisticalController {

    private Pane thongKePane;
    private static TableView<StatisticalFilm> tableThongKe;
    @FXML private TableColumn<StatisticalFilm, String> filmNameColumn;
    @FXML private TableColumn<StatisticalFilm, Integer> showCountColumn;
    @FXML private TableColumn<StatisticalFilm, Integer> seatSoldColumn;
    @FXML private TableColumn<StatisticalFilm, Double> totalRevenueColumn;
    @FXML private static DatePicker batDauThongKe;
    @FXML private static DatePicker ketThucThongKe;
    @FXML private static TextField searchFilmField;
    @FXML private static Label totalShowLabel;
    @FXML private static Label totalSeatsLabel;
    @FXML private static Label totalRevenueLabel;
    @FXML private static PieChart pieChart;
    @FXML private static BarChart<String, Number> barChart;
    public ManageStatisticalController(MainManagerController main)
    {
        thongKePane = main.getThongKePane();
        tableThongKe = main.getTableThongKe();
        filmNameColumn = main.getFilmNameColumn();
        showCountColumn = main.getShowCountColumn();
        seatSoldColumn = main.getSeatSoldColumn();
        totalRevenueColumn = main.getTotalRevenueColumn();
        batDauThongKe = main.getBatDauThongKe();
        ketThucThongKe = main.getKetThucThongKe();
        searchFilmField = main.getSearchFilmField();
        totalShowLabel = main.getTotalShowLabel();
        totalSeatsLabel = main.getTotalSeatsLabel();
        totalRevenueLabel = main.getTotalRevenueLabel();
        pieChart = main.getPieChart();
        barChart = main.getBarChart();
    }



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
    public static void onSearchThongKe(ActionEvent event) {
        loadData(null);
    }

    @FXML
    public static void onSearchFilmRevenue(ActionEvent event) {
        String keyword = searchFilmField.getText().trim();
        loadData(keyword);
    }

    private static void loadData(String keyword) {
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
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedRevenue = currencyFormatter.format(totalRevenue);

            // Nếu bạn muốn bỏ ký hiệu ₫ và thay bằng "VNĐ":
            formattedRevenue = formattedRevenue.replace("₫", "VNĐ");

            totalRevenueLabel.setText(formattedRevenue);

            updateCharts(data);
        } catch (SQLException e) {
            e.printStackTrace();
            CustomAlert.showError("Lỗi", "Không thể truy xuất dữ liệu thống kê.");
        }
    }

    private static void updateCharts(List<StatisticalFilm> data) {
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

        // Làm label của PieChart màu trắng (nếu render chậm thì cần Platform.runLater)
        Platform.runLater(() -> {
            for (PieChart.Data d : pieChart.getData()) {
                Node node = d.getNode();
                if (node != null) {
                    Text text = (Text) node.lookup(".chart-pie-label");
                    if (text != null) {
                        text.setFill(Color.WHITE);
                    }
                }
            }
        });
    }

    @FXML
    public static void onExportPdf(ActionEvent event) {
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
            // Định dạng tên file theo ngày
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String fileName = "ThongKe_" + startDate.format(formatter) + "_den_" + endDate.format(formatter) + ".pdf";
            String filePath = fileName;

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Load font Unicode hỗ trợ tiếng Việt
            InputStream fontStream = ManageStatisticalController.class.getResourceAsStream("/fonts/DejaVuLGCSerif.ttf");
            if (fontStream == null) {
                throw new FileNotFoundException("Không tìm thấy font DejaVuSerif.ttf trong resources/fonts/");
            }

            BaseFont baseFont = BaseFont.createFont(
                    "DejaVuSerif.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
                    BaseFont.CACHED, fontStream.readAllBytes(), null
            );

            Font fontTitle = new Font(baseFont, 18, Font.BOLD);
            Font fontNormal = new Font(baseFont, 12);

            Paragraph title = new Paragraph("BÁO CÁO THỐNG KÊ DOANH THU PHIM\n", fontTitle);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            Paragraph dateInfo = new Paragraph("Từ ngày " + startDate + " đến ngày " + endDate + "\n\n", fontNormal);
            dateInfo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(dateInfo);

            PdfPTable table = new PdfPTable(4);
            table.setWidths(new int[]{3, 2, 2, 3});
            table.setWidthPercentage(100);

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            currencyFormat.setMaximumFractionDigits(0);

            table.addCell(new PdfPCell(new Phrase("Tên phim", fontNormal)));
            table.addCell(new PdfPCell(new Phrase("Số suất chiếu", fontNormal)));
            table.addCell(new PdfPCell(new Phrase("Số vé bán", fontNormal)));
            table.addCell(new PdfPCell(new Phrase("Doanh thu", fontNormal)));

            for (StatisticalFilm sf : data) {
                table.addCell(new PdfPCell(new Phrase(sf.getFilmName(), fontNormal)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(sf.getShowCount()), fontNormal)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(sf.getSeatSold()), fontNormal)));
                table.addCell(new PdfPCell(new Phrase(currencyFormat.format(sf.getTotalRevenue()), fontNormal)));
            }

            document.add(table);

            // Tính tổng
            int totalShow = data.stream().mapToInt(StatisticalFilm::getShowCount).sum();
            int totalSeat = data.stream().mapToInt(StatisticalFilm::getSeatSold).sum();
            double totalRevenue = data.stream().mapToDouble(StatisticalFilm::getTotalRevenue).sum();

            Paragraph totals = new Paragraph(String.format(
                    "\nTổng cộng: %d suất chiếu, %d vé bán, %s doanh thu",
                    totalShow, totalSeat, currencyFormat.format(totalRevenue)
            ), fontNormal);
            document.add(totals);

            document.close();

            CustomAlert.showSuccess("Thành công", "Xuất file PDF thành công:\n" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            CustomAlert.showError("Lỗi", "Không thể xuất file PDF.");
        }
    }


    @FXML
    public static void onExportExcel(ActionEvent event) {
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

            // Định dạng tên file theo ngày
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String fileName = "ThongKe_" + startDate.format(formatter) + "_den_" + endDate.format(formatter) + ".xlsx";
            String filePath = fileName;

            // Ghi file Excel
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
