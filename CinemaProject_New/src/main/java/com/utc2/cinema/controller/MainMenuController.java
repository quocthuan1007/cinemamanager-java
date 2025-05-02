package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.dao.MovieShowDao;
import com.utc2.cinema.model.entity.*;
import com.utc2.cinema.service.FilmService;
import com.utc2.cinema.service.UserService;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainMenuController implements Initializable {

    @FXML
    private TextField addressConfirm;

    @FXML
    private DatePicker birthConfirm;

    @FXML
    private Button buyBtn;

    @FXML
    private Pane buyForm;

    @FXML
    private Button closeConfirm;

    @FXML
    private ChoiceBox<String> genderConfirm;

    @FXML
    private Button introBtn;

    @FXML
    private Pane introForm;
    @FXML
    private Button mainMenuBtn;

    @FXML
    private Pane mainMenuForm;

    @FXML
    private Button movieBtn;

    @FXML
    private Pane movieForm;
    @FXML
    private Pane showfilmdetail;
    @FXML
    private Button showfilmdetailBtn;
    @FXML
    private FlowPane moviePosters;

    @FXML
    private FlowPane moviePosters1;

    @FXML
    private TextField nameConfirm;

    @FXML
    private TextField numberConfirm;

    @FXML
    private ImageView posterImage;

    @FXML
    private Button saveConfirm;

    @FXML
    private Button scheduleBtn;

    @FXML
    private Pane scheduleForm;

    @FXML
    private Pane infoContainer;
    @FXML
    private Label userMain;
    @FXML private Label filmNameLabel;
    @FXML private Label filmDirectorLabel;
    @FXML private Label filmActorLabel;
    @FXML private Label filmTypeLabel;
    @FXML private Label filmReleaseDateLabel;
    @FXML private Label filmLengthLabel;
    @FXML private Label filmAgeLimitLabel;
    @FXML private Label filmContentLabel;
    @FXML private ImageView filmPosterImageView;
    @FXML private WebView webView; // WebView để hiển thị trailer
    @FXML
    private FlowPane dateFlowPane;
    @FXML
    private VBox scheduleContainer;


    @FXML private Label filmNameLabel1;
    @FXML private Label showTimeLabel;
    @FXML private Label roomLabel;
    @FXML private Label seatLabel;
    @FXML private Label comboLabel;
    @FXML private Label totalLabel;
    @FXML private Pane billPane;
    @FXML private Pane paySuccessPane;
    @FXML private VBox scheduleContainerOfFilm;
    @FXML private VBox filmsContainer;
    @FXML private VBox filmListVBox;
    @FXML private VBox scheduleListVBox;
    @FXML private AnchorPane seatSelectionPane;
    @FXML private Button backButton;
    @FXML private GridPane seatGrid;
    @FXML private Label screenLabel;
    @FXML private VBox foodDrinkVBox;
    @FXML private TableView<FoodOrder> foodDrinkTableView;
    @FXML private TableColumn<FoodOrder, String> nameColumn;
    @FXML private TableColumn<FoodOrder, String> descriptionColumn;
    @FXML private TableColumn<FoodOrder, Float> priceColumn;
    @FXML private TableColumn<FoodOrder, Integer> quantityColumn;
    @FXML private TableColumn<FoodOrder, Float> totalColumn;

    @FXML private TableView<Invoice> invoiceTable;
    @FXML private TableColumn<Invoice, String> colNgay;
    @FXML private TableColumn<Invoice, String> colSuatChieu;
    @FXML private TableColumn<Invoice, String> colPhim;
    @FXML private TableColumn<Invoice, String> colPhong;
    @FXML private TableColumn<Invoice, String> colSoGhe;
    @FXML private TableColumn<Invoice, String> colGiaTri;

    @FXML private TextField dateRangeField;
    @FXML private Label greetingLabel;

    // Getter methods
    public TableView<Invoice> getInvoiceTable() {
        return invoiceTable;
    }

    public TableColumn<Invoice, String> getColNgay() {
        return colNgay;
    }

    public TableColumn<Invoice, String> getColSuatChieu() {
        return colSuatChieu;
    }

    public TableColumn<Invoice, String> getColPhim() {
        return colPhim;
    }

    public TableColumn<Invoice, String> getColPhong() {
        return colPhong;
    }

    public TableColumn<Invoice, String> getColSoGhe() {
        return colSoGhe;
    }

    public TableColumn<Invoice, String> getColGiaTri() {
        return colGiaTri;
    }

    public TextField getDateRangeField() {
        return dateRangeField;
    }

    public Label getGreetingLabel() {
        return greetingLabel;
    }

    // Getter methods
    public Label getFilmNameLabel1() {
        return filmNameLabel1;
    }

    public Label getShowTimeLabel() {
        return showTimeLabel;
    }

    public Label getRoomLabel() {
        return roomLabel;
    }

    public Label getSeatLabel() {
        return seatLabel;
    }

    public Label getComboLabel() {
        return comboLabel;
    }

    public Label getTotalLabel() {
        return totalLabel;
    }

    public Pane getBillPane() {
        return billPane;
    }

    public Pane getPaySuccessPane() {
        return paySuccessPane;
    }

    public VBox getScheduleContainerOfFilm() {
        return scheduleContainerOfFilm;
    }

    public VBox getFilmsContainer() {
        return filmsContainer;
    }

    public VBox getFilmListVBox() {
        return filmListVBox;
    }

    public VBox getScheduleListVBox() {
        return scheduleListVBox;
    }

    public AnchorPane getSeatSelectionPane() {
        return seatSelectionPane;
    }

    public Button getBackButton() {
        return backButton;
    }

    public GridPane getSeatGrid() {
        return seatGrid;
    }

    public Label getScreenLabel() {
        return screenLabel;
    }

    public VBox getFoodDrinkVBox() {
        return foodDrinkVBox;
    }

    public TableView<FoodOrder> getFoodDrinkTableView() {
        return foodDrinkTableView;
    }

    public TableColumn<FoodOrder, String> getNameColumn() {
        return nameColumn;
    }

    public TableColumn<FoodOrder, String> getDescriptionColumn() {
        return descriptionColumn;
    }

    public TableColumn<FoodOrder, Float> getPriceColumn() {
        return priceColumn;
    }

    public TableColumn<FoodOrder, Integer> getQuantityColumn() {
        return quantityColumn;
    }

    public TableColumn<FoodOrder, Float> getTotalColumn() {
        return totalColumn;
    }


    public TextField getAddressConfirm() {
        return addressConfirm;
    }

    public DatePicker getBirthConfirm() {
        return birthConfirm;
    }

    public Button getBuyBtn() {
        return buyBtn;
    }

    public Pane getBuyForm() {
        return buyForm;
    }

    public Button getCloseConfirm() {
        return closeConfirm;
    }

    public ChoiceBox<String> getGenderConfirm() {
        return genderConfirm;
    }

    public Button getIntroBtn() {
        return introBtn;
    }

    public Pane getIntroForm() {
        return introForm;
    }

    public Button getMainMenuBtn() {
        return mainMenuBtn;
    }

    public Pane getMainMenuForm() {
        return mainMenuForm;
    }

    public Button getMovieBtn() {
        return movieBtn;
    }

    public Pane getMovieForm() {
        return movieForm;
    }

    public Pane getShowfilmdetail() {
        return showfilmdetail;
    }

    public Button getShowfilmdetailBtn() {
        return showfilmdetailBtn;
    }

    public FlowPane getMoviePosters() {
        return moviePosters;
    }

    public FlowPane getMoviePosters1() {
        return moviePosters1;
    }

    public TextField getNameConfirm() {
        return nameConfirm;
    }

    public TextField getNumberConfirm() {
        return numberConfirm;
    }

    public ImageView getPosterImage() {
        return posterImage;
    }

    public Button getSaveConfirm() {
        return saveConfirm;
    }

    public Button getScheduleBtn() {
        return scheduleBtn;
    }

    public Pane getScheduleForm() {
        return scheduleForm;
    }

    public Pane getInfoContainer() {
        return infoContainer;
    }

    public Label getUserMain() {
        return userMain;
    }

    public Label getFilmNameLabel() {
        return filmNameLabel;
    }

    public Label getFilmDirectorLabel() {
        return filmDirectorLabel;
    }

    public Label getFilmActorLabel() {
        return filmActorLabel;
    }

    public Label getFilmTypeLabel() {
        return filmTypeLabel;
    }

    public Label getFilmReleaseDateLabel() {
        return filmReleaseDateLabel;
    }

    public Label getFilmLengthLabel() {
        return filmLengthLabel;
    }

    public Label getFilmAgeLimitLabel() {
        return filmAgeLimitLabel;
    }

    public Label getFilmContentLabel() {
        return filmContentLabel;
    }

    public ImageView getFilmPosterImageView() {
        return filmPosterImageView;
    }

    public WebView getWebView() {
        return webView;
    }

    public FlowPane getDateFlowPane() {
        return dateFlowPane;
    }

    public VBox getScheduleContainer() {
        return scheduleContainer;
    }
    /// //////////////////////Main_code///////////////////////// ///
    private FilmDisplayController filmDisplayController;
    private ScheduleDisplayController scheduleDisplayController;
    private UserConfirmController userConfirmController;
    private BuyTicketController buyTicketController;
    private  HistoryController historyController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMainForm();

        userConfirmController = new UserConfirmController(this);
        userConfirmController.setupUser();
        userConfirmController.setupGenderComboBox();

        scheduleDisplayController = new ScheduleDisplayController(this);
        scheduleDisplayController.setupSchedule();
        
        filmDisplayController = new FilmDisplayController(this);
        filmDisplayController.setupFilms();

        buyTicketController = new BuyTicketController(this);
        buyTicketController.initialize();

        historyController = new HistoryController(this);
        historyController.initialize();

    }
    ///  confirm-info
    @FXML
    void onPlayerClickInfoConfirm(MouseEvent event){
        userConfirmController.onPlayerClickInfoConfirm(event);
    }
    @FXML
    void onPlayerSaveInfoConfirm(ActionEvent event) {
        userConfirmController.onPlayerSaveInfoConfirm(event);
    }
    @FXML
    void onPlayerCloseInfoConfirm(ActionEvent event) {
        userConfirmController.onPlayerCloseInfoConfirm(event);
    }

    @FXML
    void onBack() {
        buyTicketController.onBack(); // ủy quyền xử lý qua controller thật sự
    }
    @FXML
    void onBackToSchedule() {
        buyTicketController.onBackToSchedule();
    }

    @FXML
    void onPay() {
        buyTicketController.onPay();
    }

    @FXML
    void onAppcetPay() {
        buyTicketController.onAppcetPay();
    }



    private void setupMainForm() {
        mainMenuForm.setVisible(true);
        introForm.setVisible(false);
        movieForm.setVisible(false);
        scheduleForm.setVisible(false);
        buyForm.setVisible(false);
    }
    /// -- ///
    @FXML
    void switchButton(MouseEvent event) {

        // Ẩn tất cả trước
        mainMenuForm.setVisible(false);
        introForm.setVisible(false);
        movieForm.setVisible(false);
        scheduleForm.setVisible(false);
        buyForm.setVisible(false);
        showfilmdetail.setVisible(false);

        if (event.getSource() == mainMenuBtn) {
            mainMenuForm.setVisible(true);
        } else if (event.getSource() == movieBtn) {
            movieForm.setVisible(true);
        } else if (event.getSource() == scheduleBtn) {
            scheduleForm.setVisible(true);
        } else if (event.getSource() == buyBtn) {
            buyForm.setVisible(true);
        } else if (event.getSource() == introBtn) {
            historyController.initialize();
            introForm.setVisible(true);
        } else if (event.getSource() == showfilmdetailBtn) {
            showfilmdetail.setVisible(true);
        }
    }
}
