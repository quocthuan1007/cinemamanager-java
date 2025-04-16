package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Film;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        mainMenuForm.setVisible(true);
        introForm.setVisible(false);
        movieForm.setVisible(false);
        scheduleForm.setVisible(false);
        buyForm.setVisible(false);

        List<Film> films = filmService.getAllFilms();
        showFilms(films);

        if (UserSession.getInstance() != null) {
            String email = UserSession.getInstance().getEmail();
            userMain.setText(email);
        } else {
            userMain.setText("No user.");
        }
        if (genderConfirm.getItems().isEmpty()) {
            genderConfirm.getItems().addAll("Nam", "Nữ");
        }
    }
    @FXML
    void switchButton(MouseEvent event) {
        if(event.getSource() == mainMenuBtn)
        {
            mainMenuForm.setVisible(true);
            introForm.setVisible(false);
            movieForm.setVisible(false);
            scheduleForm.setVisible(false);
            buyForm.setVisible(false);

        }
        else if(event.getSource() == movieBtn)
        {
            movieForm.setVisible(true);
            mainMenuForm.setVisible(false);
            introForm.setVisible(false);
            scheduleForm.setVisible(false);
            buyForm.setVisible(false);

        }
        else if(event.getSource() == scheduleBtn)
        {
            scheduleForm.setVisible(true);
            movieForm.setVisible(false);
            mainMenuForm.setVisible(false);
            introForm.setVisible(false);
            buyForm.setVisible(false);
        }
        else if(event.getSource() == buyBtn)
        {
            buyForm.setVisible(true);
            movieForm.setVisible(false);
            mainMenuForm.setVisible(false);
            introForm.setVisible(false);
            scheduleForm.setVisible(false);
        }
        else if(event.getSource() == introBtn)
        {
            introForm.setVisible(true);
            movieForm.setVisible(false);
            mainMenuForm.setVisible(false);
            scheduleForm.setVisible(false);
            buyForm.setVisible(false);
        }
    }
    private final FilmService filmService = new FilmService();

    private VBox createFilmBox(Film film) {
        String posterPath = "/Image/" + film.getPosterUrl() + ".png";
        InputStream is = getClass().getResourceAsStream(posterPath);
        if (is == null) {
            System.out.println("Không tìm thấy ảnh: " + posterPath);
            return null;
        }

        Image image = new Image(is);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(160);
        imageView.setFitHeight(190);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        Label nameLabel = new Label(film.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label directorLabel = new Label("Đạo diễn: " + film.getDirector());

        Button bookButton = new Button("🎟️ Đặt vé");
        bookButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");
        bookButton.setOnAction(event -> {
            System.out.println("Hiển thị thông tin chi tiết cho phim: " + film.getName());
            openFilmDetailWindow(film);
        });

        VBox filmBox = new VBox(8, imageView, nameLabel, directorLabel, bookButton);
        filmBox.setAlignment(Pos.CENTER);
        filmBox.setPadding(new Insets(10));
        filmBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");
        filmBox.setPrefWidth(180);

        return filmBox;
    }

    private void showFilms(List<Film> films) {
        int count = 0;
        for (Film film : films) {
            if (count >= 8) break;

            try {
                VBox filmBox1 = createFilmBox(film);
                VBox filmBox2 = createFilmBox(film); // mỗi nơi 1 box riêng

                if (filmBox1 != null) moviePosters.getChildren().add(filmBox1);
                if (filmBox2 != null) moviePosters1.getChildren().add(filmBox2);

                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void openFilmDetailWindow(Film film) {
        try {
            // Tải FXML của cửa sổ chi tiết phim
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ShowFilmDetail.fxml"));
            AnchorPane filmDetailPane = loader.load();  // Chuyển sang AnchorPane nếu FXML sử dụng AnchorPane

            // Lấy controller của giao diện chi tiết phim và truyền thông tin bộ phim
            ShowFilmDetailController controller = loader.getController();
            controller.setFilmDetails(film); // Truyền thông tin phim vào controller của giao diện chi tiết phim

            // Tạo cửa sổ mới để hiển thị chi tiết bộ phim
            Stage stage = new Stage();
            stage.setTitle("Thông tin chi tiết phim: " + film.getName());
            stage.setScene(new Scene(filmDetailPane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
