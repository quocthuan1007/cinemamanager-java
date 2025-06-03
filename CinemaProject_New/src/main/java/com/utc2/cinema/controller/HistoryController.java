package com.utc2.cinema.controller;

import com.utc2.cinema.dao.BillDao;
import com.utc2.cinema.dao.FilmRatingDao;
import com.utc2.cinema.model.entity.FilmRating;
import com.utc2.cinema.model.entity.Invoice;
import com.utc2.cinema.model.entity.User;
import com.utc2.cinema.model.entity.UserSession;
import com.utc2.cinema.service.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HistoryController {
    private MainMenuController mainMenu = new MainMenuController();
    @FXML private TableView<Invoice> invoiceTable;
    @FXML private TableColumn<Invoice, String> colNgay;
    @FXML private TableColumn<Invoice, String> colSuatChieu;
    @FXML private TableColumn<Invoice, String> colPhim;
    @FXML private TableColumn<Invoice, String> colPhong;
    @FXML private TableColumn<Invoice, String> colSoGhe;
    @FXML private TableColumn<Invoice, String> colGiaTri;

    @FXML private Label filmNameLabel2;
    @FXML private Label showTimeLabel2;
    @FXML private Label roomLabel2;
    @FXML private Label seatLabel2;
    @FXML private Label comboLabel2;
    @FXML private Label totalLabel2;

    @FXML private Pane billPane2;

    @FXML private ImageView star1, star2, star3, star4, star5;
    @FXML private TextField commentField;
    @FXML private Button submitRatingButton;
    @FXML private Button viewBillButton;

    private final BillDao billDao = new BillDao();
    private final FilmRatingDao filmRatingDao = new FilmRatingDao();

    private final ObservableList<Invoice> invoiceData = FXCollections.observableArrayList();

    private final Image filledStar = new Image(getClass().getResourceAsStream("/image/sao-vang.png"));
    private final Image emptyStar = new Image(getClass().getResourceAsStream("/image/sao.png"));

    private int currentRating = 0;
    private Invoice selectedInvoice = null;  // Lưu hóa đơn đang chọn để đánh giá


    public HistoryController(MainMenuController mainMenu) {
        this.mainMenu = mainMenu;

        // gán các thành phần UI từ mainMenu như bạn đã làm...
        this.invoiceTable = mainMenu.getInvoiceTable();
        this.colNgay = mainMenu.getColNgay();
        this.colSuatChieu = mainMenu.getColSuatChieu();
        this.colPhim = mainMenu.getColPhim();
        this.colPhong = mainMenu.getColPhong();
        this.colSoGhe = mainMenu.getColSoGhe();
        this.colGiaTri = mainMenu.getColGiaTri();
        // Nếu muốn truy cập các thành phần UI từ mainMenu thì gán như sau (nếu bạn đã có getter)
        this.filmNameLabel2 = mainMenu.getFilmNameLabel1();
        this.showTimeLabel2 = mainMenu.getShowTimeLabel();
        this.roomLabel2 = mainMenu.getRoomLabel();
        this.seatLabel2 = mainMenu.getSeatLabel();
        this.comboLabel2 = mainMenu.getComboLabel();
        this.totalLabel2 = mainMenu.getTotalLabel();
        this.billPane2 = mainMenu.getBillPane();
        this.submitRatingButton =mainMenu.getSubmitRatingButton();
        this.viewBillButton = mainMenu.getViewBillButton();
        this.commentField = mainMenu.getCommentField();
        this.star1= mainMenu.getStar1();
        this.star2= mainMenu.getStar2();
        this.star3= mainMenu.getStar3();
        this.star4= mainMenu.getStar4();
        this.star5= mainMenu.getStar5();

    }
    @FXML
    void initialize() {
        // Setup bảng
        colNgay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatePurchased().toString()));
        colSuatChieu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovieShow()));
        colPhim.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovieTitle()));
        colPhong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoomName()));
        colSoGhe.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSeatCount())));
        colGiaTri.setCellValueFactory(cellData -> new SimpleStringProperty(formatCurrency(cellData.getValue().getTotalCost())));

        User Info = UserService.getUser(UserSession.getInstance().getUserId());
        int userID = Info.getId();
        List<Invoice> invoices = billDao.getInvoicesByUserAndDateRange(userID);
        invoiceData.setAll(invoices);
        invoiceTable.setItems(invoiceData);

        billPane2.setVisible(false);
        submitRatingButton.setDisable(true);
        viewBillButton.setDisable(true);
        updateStarDisplay(0);

        // Thêm listener chọn hóa đơn
        invoiceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedInvoice = newSelection;
                viewBillButton.setDisable(false);
                submitRatingButton.setDisable(false);

//                int userId = 5; // TODO: Thay bằng UserSession nếu cần
                int filmId = selectedInvoice.getFilmId();

                FilmRating existingRating = filmRatingDao.getRatingByUserAndFilm(userID, filmId);
                if (existingRating != null) {
                    currentRating = existingRating.getRating();
                    updateStarDisplay(currentRating);
                    commentField.setText(existingRating.getReview());
                } else {
                    currentRating = 0;
                    updateStarDisplay(0);
                    commentField.clear();
                }
            } else {
                selectedInvoice = null;
                currentRating = 0;
                updateStarDisplay(0);
                commentField.clear();
                viewBillButton.setDisable(true);
                submitRatingButton.setDisable(true);
            }
        });
        viewBillButton.setOnAction(e -> handleViewBillAction());
        submitRatingButton.setOnAction(event -> handleSubmitFeedback());
    }

    private void showBillDetail(Invoice invoice) {
        mainMenu.getFilmNameLabel2().setText(invoice.getMovieTitle());
        mainMenu.getShowTimeLabel2().setText(invoice.getMovieShow());
        mainMenu.getRoomLabel2().setText(invoice.getRoomName());
        mainMenu.getSeatLabel2().setText(invoice.getSeatNumbers());
        mainMenu.getComboLabel2().setText(invoice.getFoodCombo());
        mainMenu.getTotalLabel2().setText(formatCurrency(invoice.getTotalCost()));

        mainMenu.getBillPane2().setVisible(true);
//        billPane2.setVisible(true);
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }


    void handleViewBillAction() {
        Invoice invoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (invoice != null) {
            selectedInvoice = invoice;
            showBillDetail(invoice);

        } else {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một hóa đơn để xem chi tiết.");
        }
    }


    void onBack1() {
        mainMenu.getBillPane2().setVisible(false);
//        billPane2.setVisible(false);
        submitRatingButton.setDisable(true);
        selectedInvoice = null;
        currentRating = 0;
        updateStarDisplay(0);
    }

    private void updateStarDisplay(int rating) {
        ImageView[] stars = { star1, star2, star3, star4, star5 };
        for (int i = 0; i < stars.length; i++) {
            stars[i].setImage(i < rating ? filledStar : emptyStar);
        }
    }

    // Các sự kiện click sao
    @FXML
    void handleStarClick1() { setRating(1); }
    @FXML
    void handleStarClick2() { setRating(2); }
    @FXML
    void handleStarClick3() { setRating(3); }
    @FXML
    void handleStarClick4() { setRating(4); }
    @FXML
    void handleStarClick5() { setRating(5); }

    private void setRating(int rating) {
        if (selectedInvoice == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn hóa đơn", "Vui lòng chọn một hóa đơn trước khi đánh giá.");
            return;
        }
        currentRating = rating;
        updateStarDisplay(rating);
    }

    private void handleSubmitFeedback() {
        if (selectedInvoice == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn hóa đơn", "Vui lòng chọn một hóa đơn để đánh giá.");
            return;
        }

        String comment = commentField.getText().trim();

        if (currentRating == 0) {
            showAlert(Alert.AlertType.WARNING, "Chưa đánh giá sao", "Vui lòng chọn số sao để đánh giá.");
            return;
        }
        User Info = UserService.getUser(UserSession.getInstance().getUserId());
        int userID = Info.getId();
        Invoice invoice = invoiceTable.getSelectionModel().getSelectedItem();
        selectedInvoice = invoice;
        int filmId = selectedInvoice.getFilmId();   // Lấy filmId từ hóa đơn
        System.out.println("Saving rating: userId = " + userID + ", filmId = " + filmId + ", rating = " + currentRating);
        boolean success = filmRatingDao.saveRating(userID, filmId, currentRating, comment);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã gửi đánh giá thành công.");
            selectedInvoice.setRating(currentRating);
            commentField.clear();
            currentRating = 0;
            updateStarDisplay(0);
        } else {
            showAlert(Alert.AlertType.ERROR, "Thất bại", "Không thể gửi đánh giá. Vui lòng thử lại.");
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
