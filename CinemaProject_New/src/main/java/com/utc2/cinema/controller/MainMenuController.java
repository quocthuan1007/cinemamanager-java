package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.dao.MovieShowDao;
import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.model.entity.MovieShow;
import com.utc2.cinema.model.entity.User;
import com.utc2.cinema.model.entity.UserSession;
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
    private Pane infoForm;
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

    public Pane getInfoForm() {
        return infoForm;
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMainForm();
        setupUser();
        setupGenderComboBox();

        scheduleDisplayController = new ScheduleDisplayController(this);
        scheduleDisplayController.setupSchedule();
        
        filmDisplayController = new FilmDisplayController(this);
        filmDisplayController.setupFilms();
    }

    private void setupMainForm() {
        mainMenuForm.setVisible(true);
        introForm.setVisible(false);
        movieForm.setVisible(false);
        scheduleForm.setVisible(false);
        buyForm.setVisible(false);
    }


    private void setupUser() {
        if (UserSession.getInstance() != null) {
            String email = UserSession.getInstance().getEmail();
            userMain.setText(email);
        } else {
            userMain.setText("No user.");
        }
    }

    private void setupGenderComboBox() {
        if (genderConfirm.getItems().isEmpty()) {
            genderConfirm.getItems().addAll("Nam", "Nữ");
        }
    }
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
            introForm.setVisible(true);
        } else if (event.getSource() == showfilmdetailBtn) {
            showfilmdetail.setVisible(true);
        }
    }

    //Mở cap nhật thông tin
    private void clearInfoInput()
    {
        nameConfirm.setText("");
        birthConfirm.setValue(null);
        genderConfirm.setValue(null);
        addressConfirm.setText("");
        numberConfirm.setText("");
    }
    private void loadUserInfo()
    {
        User Info = UserService.getUser(UserSession.getInstance().getUserId());
        if(Info == null)
        {
            nameConfirm.setPromptText("Null");
            birthConfirm.setPromptText("Null");
            genderConfirm.setValue("Null");
            addressConfirm.setPromptText("Null");
            numberConfirm.setPromptText("Null");
        }
        else {
            nameConfirm.setPromptText(Info.getName() != null ? Info.getName() : "Null");
            birthConfirm.setPromptText(Info.getBirth() != null ? Info.getBirth().toString() : "Null");
            addressConfirm.setPromptText(Info.getAddress() != null ? Info.getAddress() : "Null");
            numberConfirm.setPromptText(Info.getPhone() != null ? Info.getPhone() : "Null");
            genderConfirm.setValue(Info.isGender() ? "Nam" : "Nữ");
        }
    }
    @FXML
    void onPlayerClickInfoConfirm(MouseEvent event)
    {
        if(!infoForm.isVisible()) {
            loadUserInfo();
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), infoForm);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            infoForm.setVisible(true);
        }
    }

    @FXML
    void onPlayerCloseInfoConfirm(ActionEvent event) {
        if(infoForm.isVisible())
            infoForm.setVisible(false);
    }

    @FXML
    void onPlayerSaveInfoConfirm(ActionEvent event) {
        try {
            String name = nameConfirm.getText();
            String genderText = genderConfirm.getValue();
            java.time.LocalDate birthValue = birthConfirm.getValue(); // lấy trước
            String address = addressConfirm.getText();
            String phone = numberConfirm.getText();

            if (name.isEmpty() || genderText == null || birthValue == null || address.isEmpty() || phone.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Thiếu thông tin");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng điền đầy đủ tất cả các thông tin trước khi lưu.");
                alert.showAndWait();
                return;
            }

            boolean gender = genderText.equals("Nam");
            java.util.Date birth = java.sql.Date.valueOf(birthValue);

            int accountId = UserSession.getInstance().getUserId();
            User user = new User(0, name, gender, birth, phone, address, accountId);

            if (UserService.getUser(accountId) != null) {
                UserService.updateUser(user);
            } else {
                UserService.insertUser(user);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Thông tin đã được lưu thành công!");
            alert.showAndWait();

            clearInfoInput();
            loadUserInfo();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Lưu thông tin thất bại. Vui lòng kiểm tra lại dữ liệu.");
            alert.showAndWait();
        }
    }
}
