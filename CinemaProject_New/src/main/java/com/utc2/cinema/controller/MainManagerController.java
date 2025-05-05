package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.model.entity.*;
import com.utc2.cinema.service.AccountService;
import com.utc2.cinema.service.UserService;
import com.utc2.cinema.view.Login;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainManagerController implements Initializable {
    public TableColumn<UserAccount, String> getEmailUser() {
        return emailUser;
    }

    public TableColumn<UserAccount, String> getGenderUser() {
        return genderUser;
    }

    public TableColumn<UserAccount, String> getNameUser() {
        return nameUser;
    }

    public TableColumn<UserAccount, String> getNumUser() {
        return numUser;
    }

    public TableColumn<UserAccount, String> getAddUser() {
        return addUser;
    }

    public TableColumn<UserAccount, Date> getBirthUser() {
        return birthUser;
    }

    public TableView<UserAccount> getTableUser() {
        return tableUser;
    }

    public ObservableList<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    public TextField getAddressConfirm() {
        return addressConfirm;
    }

    public DatePicker getBirthConfirm() {
        return birthConfirm;
    }

    public Button getCloseConfirm() {
        return closeConfirm;
    }

    public ChoiceBox<String> getGenderConfirm() {
        return genderConfirm;
    }

    public Pane getInfoForm() {
        return infoForm;
    }

    public TextField getNameConfirm() {
        return nameConfirm;
    }

    public TextField getNumberConfirm() {
        return numberConfirm;
    }

    public Button getSaveConfirm() {
        return saveConfirm;
    }
    //===
    //confirm
    @FXML
    private TextField addressConfirm;

    @FXML
    private Pane filmForm;
    @FXML
    private DatePicker birthConfirm;
    @FXML
    private Button closeConfirm;
    @FXML
    private ChoiceBox<String> genderConfirm;
    @FXML
    private Pane infoForm;
    @FXML
    private TextField nameConfirm;
    @FXML
    private TextField numberConfirm;
    @FXML
    private Button saveConfirm;
    @FXML
    private ImageView adPosterImg;
    @FXML
    private ImageView posterImg;
    @FXML
    private TableColumn<UserAccount, String> addUser;

    @FXML
    private TableColumn<UserAccount, Date> birthUser;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    @FXML
    private TableColumn<UserAccount, String> emailUser;

    @FXML
    private TextField searchEmailField;

    @FXML
    private TableColumn<UserAccount, String> genderUser;

    @FXML
    private TableColumn<UserAccount, String> nameUser;

    @FXML
    private TableColumn<UserAccount, String> numUser;

    @FXML
    private VBox sidebar;

    @FXML
    private TableView<UserAccount> tableUser;

    @FXML
    private Button viewBtn;
    @FXML
    private Pane userPane;
    @FXML
    private Pane schedulePane;
    @FXML
    private Pane foodPane;
    @FXML
    private Pane movieManPane;
    @FXML
    private Pane roomPane;


    /// ////////////////////////////////////////////////
    private ManageUserController userController;
    private ManageFilmController filmController;
    private ManageFoodController foodController;
    private ManageScheduleController scheduleController;
    private  ManageRoomController roomController;
    private ManageStatisticalController statisticalController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userController = new ManageUserController(this);
        userController.setupGenderComboBox();
        userController.init();

        filmController = new ManageFilmController(this);
        filmController.init();

        foodController = new ManageFoodController(this);
        foodController.init();

        scheduleController = new ManageScheduleController(this);
        scheduleController.initialize();

        roomController =new ManageRoomController(this);
        roomController.initialize();

        statisticalController = new ManageStatisticalController(this);
        statisticalController.initialize();


        hideAllPanes();
        foodPane.setVisible(true);

        onInitialize();
    }
    void onInitialize() {
        searchFilm.textProperty().addListener((observable, oldValue, newValue) -> {
            onSearchFilm();
        });
        searchFood.textProperty().addListener((observable, oldValue, newValue) -> {
            onSearchFood();
        });
        searchEmailField.textProperty().addListener((observable, oldValue, newValue) -> {
            onSearchByEmail();
        });
    }
    /// ////////////////////////////////////////////////
    @FXML
    Pane MainManagerPane;

    @FXML
    void handleDangXuat(ActionEvent event) {
        Stage MainManager = (Stage) MainManagerPane.getScene().getWindow();
        MainManager.close();
        UserSession.getInstance().cleanUserSession();
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("/FXML/Login.fxml"));
            LoginController control = new LoginController();
            fxmlLoader.setController(control);
            Pane root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            stage.setTitle("Hello !");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Đăng xuất");
    }
    @FXML
    void handleNguoiDung(ActionEvent event) {
        System.out.println("Người dùng");
        hideAllPanes();
        userPane.setVisible(true);
    }
    @FXML
    void handlePhongChieu(ActionEvent event)
    {
        System.out.println("Phòng chiếu");
        hideAllPanes();
        roomPane.setVisible(true);
    }
    @FXML
    void handleLichChieu(ActionEvent event) {
        System.out.println("Lịch chiếu");
        hideAllPanes();
        scheduleController.initialize();
        schedulePane.setVisible(true);
    }

    @FXML
    void handleDoAn(ActionEvent event) {
        System.out.println("Đồ ăn");
        hideAllPanes();
        foodPane.setVisible(true);
    }

    @FXML
    void handleQuanLyPhim(ActionEvent event) {
        System.out.println("Quản lý phim");
        hideAllPanes();
        movieManPane.setVisible(true);
    }

    @FXML
    void handleThongKe(ActionEvent event) {
        System.out.println("Thống kê");
        hideAllPanes();
        thongKePane.setVisible(true);
    }

    // Hàm ẩn tất cả các Pane
    private void hideAllPanes() {
        userPane.setVisible(false);
        schedulePane.setVisible(false);
        foodPane.setVisible(false);
        thongKePane.setVisible(false);
        movieManPane.setVisible(false);
        roomPane.setVisible(false);
    }
    private ObservableList<UserAccount> userAccounts = FXCollections.observableArrayList();


    @FXML
    void onClickDelete(ActionEvent event)
    {
        userController.onClickDelete(event);
    }

    @FXML
    void onClickEdit(ActionEvent event) {
        userController.onClickEdit(event);
    }

    @FXML
    void onClickView(ActionEvent event) {
        userController.onClickView(event);
    }
    //confirm

    @FXML
    void onPlayerCloseInfoConfirm(ActionEvent event) {
        userController.onPlayerCloseInfoConfirm(event);
    }

    public TextField getSearchEmailField() {
        return searchEmailField;
    }

    @FXML
    void onPlayerSaveInfoConfirm(ActionEvent event) {
        userController.onPlayerSaveInfoConfirm(event);
    }
    @FXML
    void onSearchByEmail()
    {
        userController.onSearchByEmail();
    }
    //===========================================================film-manage=====================================
    //===========================================================================================================
    @FXML
    private TextField CountryFilm;

    @FXML
    private TextField actorFilm;

    @FXML
    private TableColumn<Film, String> actorFilmCol;

    @FXML
    private Button adPosterBtn;

    @FXML
    private Button addFilmBtn;

    @FXML
    private TextField ageFilm;

    @FXML
    private TextArea conFilm;

    @FXML
    private TableColumn<Film, String> countryFilmCol;

    @FXML
    private Button deleteFilmBtn;

    @FXML
    private TextField direFilm;

    @FXML
    private Button editFilmBtn;

    @FXML
    private TextField lenFilm;

    @FXML
    private TextField nameFilm;

    @FXML
    private TableColumn<Film, String> nameFilmCol;

    @FXML
    private Button posterBtn;

    @FXML
    private DatePicker releaseFilm;

    @FXML
    private TextField searchFilm;

    @FXML
    private TextField staFilm;
    @FXML
    private TableColumn<Film, String> statusFilmCol;
    @FXML
    private TableView<Film> tableFilm;
    @FXML
    private TextField trailerFilm;
    @FXML
    private Button addOrSaveBtn;
    private String adPosterPath = "";
    private String posterPath = "";

    public TextField getCountryFilm() {
        return CountryFilm;
    }

    public TextField getActorFilm() {
        return actorFilm;
    }

    public TableColumn<Film, String> getActorFilmCol() {
        return actorFilmCol;
    }

    public Button getAdPosterBtn() {
        return adPosterBtn;
    }

    public Button getAddFilmBtn() {
        return addFilmBtn;
    }

    public TextField getAgeFilm() {
        return ageFilm;
    }

    public TextArea getConFilm() {
        return conFilm;
    }

    public TableColumn<Film, String> getCountryFilmCol() {
        return countryFilmCol;
    }

    public Button getDeleteFilmBtn() {
        return deleteFilmBtn;
    }

    public TextField getDireFilm() {
        return direFilm;
    }

    public Button getEditFilmBtn() {
        return editFilmBtn;
    }

    public TextField getLenFilm() {
        return lenFilm;
    }

    public TextField getNameFilm() {
        return nameFilm;
    }

    public TableColumn<Film, String> getNameFilmCol() {
        return nameFilmCol;
    }

    public DatePicker getReleaseFilm() {
        return releaseFilm;
    }

    public TextField getSearchFilm() {
        return searchFilm;
    }

    public TextField getStaFilm() {
        return staFilm;
    }

    public TableColumn<Film, String> getStatusFilmCol() {
        return statusFilmCol;
    }
    public Pane getFilmForm() {
        return filmForm;
    }
    public TableView<Film> getTableFilm() {
        return tableFilm;
    }

    public TextField getTrailerFilm() {
        return trailerFilm;
    }

    public String getAdPosterPath() {
        return adPosterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Button getPosterBtn() {
        return posterBtn;
    }

    public void setAdPosterPath(String adPosterPath) {
        this.adPosterPath = adPosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Button getAddOrSaveBtn() {
        return addOrSaveBtn;
    }
    @FXML
    public ImageView getAdPosterImg() {
        return adPosterImg;
    }
    @FXML
    public ImageView getPosterImg() {
        return posterImg;
    }

    @FXML
    void onClickAddFilm(ActionEvent event) {
        filmController.onClickAddFilm(event);
    }

    @FXML
    void onClickDeleteFilm(ActionEvent event) {
        filmController.onClickDeleteFilm(event);
    }

    @FXML
    void onClickEditFilm(ActionEvent event) {
        filmController.onClickEditFilm(event);
    }

    @FXML
    void onClickAdPoster(ActionEvent event) {
        filmController.onClickAdPoster(event);
    }
    @FXML
    void onClickPoster(ActionEvent event) {
        filmController.onClickPoster(event);
    }
    @FXML
    void onClickViewFilm(ActionEvent event) {

    }
    @FXML
    void onSearchFilm() {
        filmController.onSearchFilm();
    }
    @FXML
    void onClickFilmFormClose(ActionEvent event) {
        if(filmForm.isVisible())
        {
            filmForm.setVisible(false);
        }
    }

    /// ////////////////////////////////////////Food-Manage////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private Button addFoodBtn;

    @FXML
    private Button addOrSaveFood;

    @FXML
    private TableColumn<Food, Float> costCol;

    @FXML
    private TextField costFood;

    @FXML
    private Button deleteFoodBtn;

    @FXML
    private TableColumn<Food, String> descripCol;

    @FXML
    private TextArea descripFood;

    @FXML
    private Button editFoodBtn;

    @FXML
    private Pane foodForm;

    @FXML
    private TableColumn<Food, String> foodCol;

    @FXML
    private TextField nameFood;

    @FXML
    private TextField searchFood;

    @FXML
    private TableView<Food> tableFood;

    public TextField getSearchFood() {
        return searchFood;
    }

    public TableView<Food> getTableFood() {
        return tableFood;
    }

    public TextField getNameFood() {
        return nameFood;
    }

    public Pane getFoodForm() {
        return foodForm;
    }

    public Pane getFoodPane() {
        return foodPane;
    }

    public TextArea getDescripFood() {
        return descripFood;
    }

    public TableColumn<Food, String> getDescripCol() {
        return descripCol;
    }

    public TextField getCostFood() {
        return costFood;
    }

    public TableColumn<Food, Float> getCostCol() {
        return costCol;
    }

    public Button getAddFoodBtn() {
        return addFoodBtn;
    }

    public Button getAddOrSaveFood() {
        return addOrSaveFood;
    }

    public Button getEditFoodBtn() {
        return editFoodBtn;
    }

    public TableColumn<Food, String> getFoodCol() {
        return foodCol;
    }

    public Button getDeleteFoodBtn() {
        return deleteFoodBtn;
    }

    @FXML
    void onClickAddFood(ActionEvent event) {
        foodController.onClickAddFood(event);
    }

    @FXML
    void onClickDeleteFood(ActionEvent event) {
        foodController.onClickDeleteFood(event);
    }

    @FXML
    void onClickEditFood(ActionEvent event) {
        foodController.onClickEditFood(event);
    }

    @FXML
    void onClickFoodFormClose(ActionEvent event)
    {
        if(foodForm.isVisible())foodForm.setVisible(false);
    }

    @FXML
    void onSearchFood() {
        foodController.onSearchFood();
    }
    /// ////////////////////////////////////////Schedule-Manage////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private TableView<MovieShow> scheduleTable;

    @FXML
    private TableColumn<MovieShow, String> colStart;

    @FXML
    private TableColumn<MovieShow, String> colEnd;

    @FXML
    private TableColumn<MovieShow, String> colFilm;

    @FXML
    private TableColumn<MovieShow, Integer> colRoom;

    @FXML
    private TableColumn<MovieShow, Void> colDelete;

    @FXML
    private Pane confirmDeletePane;

    @FXML
    private Pane removePane;

    @FXML
    private Pane RemoveSuccessPane;

    @FXML
    private Pane addMovieShowPane;

    @FXML
    private ComboBox<Film> filmComboBox;

    @FXML
    private ComboBox<Integer> roomComboBox;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;
    @FXML
    private TextField searchField;


// GETTERS

    public TableView<MovieShow> getScheduleTable() {
        return scheduleTable;
    }

    public TableColumn<MovieShow, String> getColStart() {
        return colStart;
    }

    public TableColumn<MovieShow, String> getColEnd() {
        return colEnd;
    }

    public TableColumn<MovieShow, String> getColFilm() {
        return colFilm;
    }

    public TableColumn<MovieShow, Integer> getColRoom() {
        return colRoom;
    }

    public TableColumn<MovieShow, Void> getColDelete() {
        return colDelete;
    }

    public Pane getConfirmDeletePane() {
        return confirmDeletePane;
    }

    public Pane getRemovePane() {
        return removePane;
    }

    public Pane getRemoveSuccessPane() {
        return RemoveSuccessPane;
    }

    public Pane getAddMovieShowPane() {
        return addMovieShowPane;
    }

    public ComboBox<Film> getFilmComboBox() {
        return filmComboBox;
    }

    public ComboBox<Integer> getRoomComboBox() {
        return roomComboBox;
    }

    public TextField getStartTimeField() {
        return startTimeField;
    }

    public TextField getEndTimeField() {
        return endTimeField;
    }
    @FXML
    void onCancelDelete() {
        scheduleController.onCancelDelete();
    }
    @FXML
    void onConfirmDelete() {
        scheduleController.onConfirmDelete();
    }
    @FXML
    void onBackMannagerSchedule() {
        scheduleController.onBackMannagerSchedule();
    }
    @FXML
    void onSaveMovieShow() {
        scheduleController.onSaveMovieShow();
    }
    @FXML
    void onCancelAddMovieShow() {
        scheduleController.onCancelAddMovieShow();
    }
    @FXML
    void onAddShowClick(){
        scheduleController.onAddShowClick();
    }
    public TextField getSearchField() {
        return searchField;
    }
    /// ////////////////////////////////////////Room-Manage////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private TextField roomNameField;

    @FXML
    private TextField rowCountField;

    @FXML
    private TextField columnCountField;

    @FXML
    private ComboBox<String> roomStatusComboBox;

    @FXML
    private GridPane seatGrid;

    @FXML
    private Button addRoomBtn;

    @FXML
    private Button deleteRoomBtn;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> roomNameComboBox;

    @FXML
    private Pane deleteConfirmationPane;

    public TextField getRoomNameField() {
        return roomNameField;
    }

    public TextField getRowCountField() {
        return rowCountField;
    }

    public TextField getColumnCountField() {
        return columnCountField;
    }

    public ComboBox<String> getRoomStatusComboBox() {
        return roomStatusComboBox;
    }

    public GridPane getSeatGrid() {
        return seatGrid;
    }

    public Button getAddRoomBtn() {
        return addRoomBtn;
    }

    public Button getDeleteRoomBtn() {
        return deleteRoomBtn;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public ComboBox<String> getRoomNameComboBox() {
        return roomNameComboBox;
    }

    public Pane getDeleteConfirmationPane() {
        return deleteConfirmationPane;
    }
    @FXML
    void onCancelDelete1(){
        roomController.onCancelDelete();
    }

    @FXML
    void onConfirmDelete1(){
        roomController.onConfirmDelete();
    }

    /// ////////////////////////-thong-ke-///////////////////////////////////////////
    /// ////////////////////////-thong-ke-///////////////////////////////////////////

    @FXML
    void onSearchMovieShow(){
        scheduleController.onSearchMovieShow();
    }

    @FXML
    private Pane thongKePane;

    public Pane getThongKePane() {
        return thongKePane;
    }
    @FXML
    private DatePicker batDauThongKe;

    @FXML
    private TableColumn<Statistical, String> dateThongKe;

    @FXML
    private TableColumn<Statistical, Float> foodThongKe;

    @FXML
    private DatePicker ketThucThongKe;

    @FXML
    private TableColumn<Statistical, Float> sumThongKe;

    @FXML
    private TableColumn<Statistical, Float> ticketThongKe;

    @FXML
    private Button timThongKe;
    @FXML
    private TableView<Statistical> tableThongKe;

    public TableView<Statistical> getTableThongKe() {
        return tableThongKe;
    }

    public Button getTimThongKe() {
        return timThongKe;
    }

    public TableColumn<Statistical, Float> getTicketThongKe() {
        return ticketThongKe;
    }

    public TableColumn<Statistical, Float> getSumThongKe() {
        return sumThongKe;
    }

    public DatePicker getKetThucThongKe() {
        return ketThucThongKe;
    }

    public TableColumn<Statistical, Float> getFoodThongKe() {
        return foodThongKe;
    }

    public TableColumn<Statistical, String> getDateThongKe() {
        return dateThongKe;
    }

    public DatePicker getBatDauThongKe() {
        return batDauThongKe;
    }


    @FXML
    void onSearchThongKe(ActionEvent e) {
        statisticalController.onSearchThongKe(e);
    }
}
