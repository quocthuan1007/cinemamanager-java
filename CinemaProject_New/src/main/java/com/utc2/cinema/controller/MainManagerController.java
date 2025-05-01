package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.model.entity.Account;
import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.model.entity.User;
import com.utc2.cinema.model.entity.UserAccount;
import com.utc2.cinema.service.AccountService;
import com.utc2.cinema.service.UserService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

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



    /// ////////////////////////////////////////////////
    private ManageUserController userController;
    private ManageFilmController filmController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userController = new ManageUserController(this);
        userController.init();

        filmController = new ManageFilmController(this);
        filmController.init();

        onInitialize();
    }
    void onInitialize() {
        searchFilm.textProperty().addListener((observable, oldValue, newValue) -> {
            onSearchFilm();
        });
        searchEmailField.textProperty().addListener((observable, oldValue, newValue) -> {
            onSearchByEmail();
        });
    }
    /// ////////////////////////////////////////////////



    @FXML
    void handleDangXuat(ActionEvent event) {
        System.out.println("Đăng xuất");
    }

    @FXML
    void handleLichChieu(ActionEvent event) {
        System.out.println("Lịch chiếu");
    }

    @FXML
    void handleNhanVien(ActionEvent event) {
        System.out.println("Nhân viên");
    }

    @FXML
    void handlePhongChieu(ActionEvent event) {
        System.out.println("Phòng chiếu");
    }

    @FXML
    void handleQuanLyPhim(ActionEvent event) {
        System.out.println("Quản lý phim");
    }

    @FXML
    void handleThongKe(ActionEvent event) {
        System.out.println("Thống kê");
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
    private TableColumn<?, ?> costCol;

    @FXML
    private TextField costFood;

    @FXML
    private Button deleteFoodBtn;

    @FXML
    private TableColumn<?, ?> descripCol;

    @FXML
    private TextArea descripFood;

    @FXML
    private Button editFoodBtn;

    @FXML
    private Pane filmForm1;

    @FXML
    private TableColumn<?, ?> foodCol;

    @FXML
    private Pane foodForm;

    @FXML
    private TextField nameFood;

    @FXML
    private TextField searchFood;

    @FXML
    private TableView<?> tableFilm1;

    @FXML
    void onClickAddFood(ActionEvent event) {

    }

    @FXML
    void onClickDeleteFood(ActionEvent event) {

    }

    @FXML
    void onClickEditFood(ActionEvent event) {

    }

    @FXML
    void onClickFoodFormClose(ActionEvent event) {

    }

    @FXML
    void onSearchFood(ActionEvent event) {

    }

}
