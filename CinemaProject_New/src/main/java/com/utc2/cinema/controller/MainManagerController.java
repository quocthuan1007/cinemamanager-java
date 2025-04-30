package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Account;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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

    //confirm
    @FXML
    private TextField addressConfirm;

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
    //
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

    private ManageUserController userController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userController = new ManageUserController(this);
        userController.init();
    }
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
    void onSearchByEmail(ActionEvent event)
    {
        userController.onSearchByEmail(event);
    }
    //
}
