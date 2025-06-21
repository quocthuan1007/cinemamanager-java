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

import javax.servlet.ServletException;

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
    private HBox moviePosters;

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
    @FXML private Film selectedFilm;
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

    @FXML private TextField searchField;
    @FXML private TextField dateRangeField;
    @FXML private Label greetingLabel;
    @FXML private ScrollPane mainShowFilm;
    // Getter methods
    public ScrollPane getMainShowFilm() {
        return mainShowFilm;
    }
    public TableView<Invoice> getInvoiceTable() {
        return invoiceTable;
    }
    public TextField getSearchField() {
        return searchField;
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

    public Pane getShowfilmDetail() {
        return showfilmdetail;
    }

    public Button getShowfilmdetailBtn() {
        return showfilmdetailBtn;
    }

    public HBox getMoviePosters() {
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

    @FXML private Label averageRatingLabel;
    @FXML private VBox ratingListContainer;
    @FXML private ScrollPane ratingScrollPane;

    public Label getAverageRatingLabel() {
        return averageRatingLabel;
    }

    public VBox getRatingListContainer() {
        return ratingListContainer;
    }

    public ScrollPane getRatingScrollPane() {
        return ratingScrollPane;
    }


    ///////history///////
    // Label
    public Label getFilmNameLabel2() {
        return filmNameLabel2;
    }
    public void setFilmNameLabel2(Label filmNameLabel2) {
        this.filmNameLabel2 = filmNameLabel2;
    }

    public Label getShowTimeLabel2() {
        return showTimeLabel2;
    }
    public void setShowTimeLabel2(Label showTimeLabel2) {
        this.showTimeLabel2 = showTimeLabel2;
    }

    public Label getRoomLabel2() {
        return roomLabel2;
    }
    public void setRoomLabel2(Label roomLabel2) {
        this.roomLabel2 = roomLabel2;
    }

    public Label getSeatLabel2() {
        return seatLabel2;
    }
    public void setSeatLabel2(Label seatLabel2) {
        this.seatLabel2 = seatLabel2;
    }

    public Label getComboLabel2() {
        return comboLabel2;
    }
    public void setComboLabel2(Label comboLabel2) {
        this.comboLabel2 = comboLabel2;
    }

    public Label getTotalLabel2() {
        return totalLabel2;
    }
    public void setTotalLabel2(Label totalLabel2) {
        this.totalLabel2 = totalLabel2;
    }

    // Pane
    public Pane getBillPane2() {
        return billPane2;
    }
    public void setBillPane2(Pane billPane2) {
        this.billPane2 = billPane2;
    }

    // ImageView (5 sao)
    public ImageView getStar1() {
        return star1;
    }
    public void setStar1(ImageView star1) {
        this.star1 = star1;
    }

    public ImageView getStar2() {
        return star2;
    }
    public void setStar2(ImageView star2) {
        this.star2 = star2;
    }

    public ImageView getStar3() {
        return star3;
    }
    public void setStar3(ImageView star3) {
        this.star3 = star3;
    }

    public ImageView getStar4() {
        return star4;
    }
    public void setStar4(ImageView star4) {
        this.star4 = star4;
    }

    public ImageView getStar5() {
        return star5;
    }
    public void setStar5(ImageView star5) {
        this.star5 = star5;
    }

    // TextField
    public TextField getCommentField() {
        return commentField;
    }
    public void setCommentField(TextField commentField) {
        this.commentField = commentField;
    }

    // Button (2 nút)
    public Button getSubmitRatingButton() {
        return submitRatingButton;
    }
    public void setSubmitRatingButton(Button submitRatingButton) {
        this.submitRatingButton = submitRatingButton;
    }

    public Button getViewBillButton() {
        return viewBillButton;
    }
    public void setViewBillButton(Button viewBillButton) {
        this.viewBillButton = viewBillButton;
    }


    public VBox getScheduleContainer() {
        return scheduleContainer;
    }
    public Film getSelectedFilm() {
        return selectedFilm;
    }
    public BuyTicketController getBuyTicketController() {
        return buyTicketController;
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

        buyTicketController = new BuyTicketController(this);
        buyTicketController.initialize();

        filmDisplayController = new FilmDisplayController(this);
        filmDisplayController.setupFilms();

        scheduleDisplayController = new ScheduleDisplayController(this);
        scheduleDisplayController.setupSchedule();
        
        historyController = new HistoryController(this);

        mainShowFilm.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Tắt thanh cuộn dọc
        mainShowFilm.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

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
    @FXML
    void  handleBookTicket(){filmDisplayController.handleBookTicket();}
    @FXML
    void onSearchFilmByName(){filmDisplayController.onSearchFilmByName();}


    @FXML
    void handleViewBillAction(){
        System.out.println("Click Xem chi tiết");
        System.out.println("historyController: " + historyController);
        if (historyController != null) {
            historyController.handleViewBillAction();
        } else {
            System.out.println("⚠️ historyController bị null!");
        }
    }
    @FXML
    void onBack1(){
        historyController.onBack1();
    }
    @FXML
    void handleStarClick1(){historyController.handleStarClick1();}
    @FXML
    void handleStarClick2(){historyController.handleStarClick2();}
    @FXML
    void handleStarClick3(){historyController.handleStarClick3();}
    @FXML
    void handleStarClick4(){historyController.handleStarClick4();}
    @FXML
    void handleStarClick5(){historyController.handleStarClick5();}
    ///film
    @FXML
    void handleBackToDetail(){filmDisplayController.handleBackToDetail();}
    @FXML
    void handleShowReviews(){filmDisplayController.handleShowReviews();}

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
        } else if (event.getSource() == introBtn)
        {
            historyController.initialize();
            introForm.setVisible(true);
        } else if (event.getSource() == showfilmdetailBtn) {
            showfilmdetail.setVisible(true);
        }
    }

}
