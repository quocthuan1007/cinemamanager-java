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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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
//        if(event.getSource() == mainMenuBtn)
//        {
//            mainMenuForm.setVisible(true);
//            introForm.setVisible(false);
//            movieForm.setVisible(false);
//            scheduleForm.setVisible(false);
//            buyForm.setVisible(false);
//
//        }
//        else if(event.getSource() == movieBtn)
//        {
//            movieForm.setVisible(true);
//            mainMenuForm.setVisible(false);
//            introForm.setVisible(false);
//            scheduleForm.setVisible(false);
//            buyForm.setVisible(false);
//
//        }
//        else if(event.getSource() == scheduleBtn)
//        {
//            scheduleForm.setVisible(true);
//            movieForm.setVisible(false);
//            mainMenuForm.setVisible(false);
//            introForm.setVisible(false);
//            buyForm.setVisible(false);
//        }
//        else if(event.getSource() == buyBtn)
//        {
//            buyForm.setVisible(true);
//            movieForm.setVisible(false);
//            mainMenuForm.setVisible(false);
//            introForm.setVisible(false);
//            scheduleForm.setVisible(false);
//        }
//        else if(event.getSource() == introBtn)
//        {
//            introForm.setVisible(true);
//            movieForm.setVisible(false);
//            mainMenuForm.setVisible(false);
//            scheduleForm.setVisible(false);
//            buyForm.setVisible(false);
//        }
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
            setFilmDetails(film);

            // Hiển thị form chi tiết phim (showFilmDetail)
            showfilmdetail.setVisible(true); // Nếu nó là phần riêng biệt trong UI của bạn
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

    public void setFilmDetails(Film film) {
        filmNameLabel.setText("Tên phim: " + film.getName());
        filmDirectorLabel.setText("Đạo diễn: " + film.getDirector());
        filmActorLabel.setText("Diễn viên: " + film.getActor());
        filmReleaseDateLabel.setText("Ngày phát hành: " + film.getReleaseDate());
        filmLengthLabel.setText("Thời lượng: " + film.getLength() + " phút");
        filmAgeLimitLabel.setText("Giới hạn tuổi: " + film.getAgeLimit() + "+");

        // Nội dung phim
        filmContentLabel.setText(film.getContent());

        // Cập nhật ảnh poster của phim
        String posterPath = "/Image/" + film.getPosterUrl() + ".png"; // Ví dụ: "inception"
        Image image = new Image(getClass().getResourceAsStream(posterPath));
        filmPosterImageView.setImage(image);

        // Hiển thị trailer
        loadTrailer(film.getTrailer());
    }

    private void loadTrailer(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) return;

        // Chuyển từ dạng https://www.youtube.com/watch?v=xxx thành https://www.youtube.com/embed/xxx
        String embedUrl = youtubeUrl.replace("watch?v=", "embed/");

        // Giảm kích thước và căn giữa iframe bên trong WebView
        String embedHTML = """
        <html>
            <body style='margin:0px;padding:0px;display:flex;justify-content:center;align-items:center;height:100%%;'>
                <iframe width='100%%' height='100%%' 
                        src='%s?autoplay=1'
                        frameborder='0' allow='autoplay; encrypted-media' allowfullscreen>
                </iframe>
            </body>
        </html>
        """.formatted(embedUrl);

        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(embedHTML);
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


    private final MovieShowDao movieShowDao = new MovieShowDao();
    private final FilmDao filmDao = new FilmDao();

    @FXML
    public void initialize() {
        LocalDate today = LocalDate.now();

        // Hiển thị ngày hôm nay mặc định
        showScheduleForDate(today);

        // Thêm các nút ngày vào FlowPane
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = today.plusDays(i);
            Button dateButton = new Button(currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dateButton.setOnAction(e -> handleDateButtonClick(currentDate));
            styleButton(dateButton);
            dateFlowPane.getChildren().add(dateButton);
        }
    }

    private void handleDateButtonClick(LocalDate date) {
        System.out.println("Ngày đã chọn: " + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        // Hiển thị lịch chiếu cho ngày đã chọn
        showScheduleForDate(date);
    }

    private void showScheduleForDate(LocalDate date) {
        // Xóa lịch chiếu cũ
        scheduleContainer.getChildren().clear();

        // Lấy các lịch chiếu cho ngày đã chọn
        List<MovieShow> movieShows = movieShowDao.getShowsByDate(date);
        if (movieShows.isEmpty()) {
            Label noShowsLabel = new Label("Không có lịch chiếu cho ngày này.");
            noShowsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #777;");
            scheduleContainer.getChildren().add(noShowsLabel);
        } else {
            // Hiển thị lịch chiếu
            displaySchedule(movieShows);
        }
    }

    private void displaySchedule(List<MovieShow> movieShows) {
        // Xóa lịch chiếu cũ
        scheduleContainer.getChildren().clear();

        // Dùng một Map để nhóm các lịch chiếu theo filmId
        Map<Integer, List<MovieShow>> movieShowMap = movieShows.stream()
                .collect(Collectors.groupingBy(MovieShow::getFilmId));

        // Lặp qua các bộ phim (filmId)
        for (Map.Entry<Integer, List<MovieShow>> entry : movieShowMap.entrySet()) {
            Integer filmId = entry.getKey();
            List<MovieShow> showsForFilm = entry.getValue();

            // Lấy thông tin phim từ filmId
            Film film = filmDao.getFilmById(filmId);
            String posterPath = "/Image/" + film.getPosterUrl() + ".png"; // Kiểm tra đúng đường dẫn ảnh
            InputStream is = getClass().getResourceAsStream(posterPath);

            if (is == null) {
                System.out.println("Không tìm thấy ảnh: " + posterPath);
                continue; // Tiếp tục với phim khác nếu không tìm thấy ảnh
            }

            Image image = new Image(is);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(160);
            imageView.setFitHeight(190);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            // Tạo VBox chứa tên phim trên poster
            VBox filmNameBox = new VBox(5);
            Label filmNameLabel = new Label(film.getName());
            filmNameLabel.setFont(new Font("Arial", 18));
            filmNameLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
            filmNameBox.getChildren().add(filmNameLabel);

            // Tạo HBox chứa poster và tên phim (tên phim trên cùng, poster dưới)
            VBox posterBox = new VBox(10);
            posterBox.setStyle("-fx-alignment: center; -fx-padding: 10px;");
            posterBox.getChildren().addAll(filmNameBox, imageView); // Poster dưới tên phim

            // Tạo VBox để chứa lịch chiếu của bộ phim
            VBox scheduleBox = new VBox(10);
            scheduleBox.setStyle("-fx-alignment: top-left; -fx-padding: 10px;");

            // Thêm lịch chiếu vào VBox
            for (MovieShow movieShowDetails : showsForFilm) {
                String showStart = movieShowDetails.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                String showEnd = movieShowDetails.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));

                String showDetails = "Bắt đầu: " + showStart + " | Kết thúc: " + showEnd;
                Label showLabel = new Label(showDetails);
                showLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px; " +
                        "-fx-background-color: transparent; -fx-border-color: #32CD32; " +
                        "-fx-border-radius: 8px; -fx-border-width: 2; " +
                        "-fx-text-fill: black;");

                scheduleBox.getChildren().add(showLabel);
            }

            // Tạo HBox để chứa cả poster và lịch chiếu
            HBox movieBox = new HBox(20);
            movieBox.setStyle("-fx-alignment: top-left; -fx-spacing: 15px;");  // Đảm bảo các thành phần không bị chồng
            movieBox.getChildren().addAll(posterBox, scheduleBox);  // Lịch chiếu nằm bên phải, poster bên trái

            // Thêm movieBox vào scheduleContainer
            scheduleContainer.getChildren().add(movieBox);
        }
    }

    private void styleButton(Button button) {
        button.setFont(new Font("Arial", 16));
        button.setTextFill(Color.BLACK);
        button.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 15; -fx-background-radius: 15;");
        button.setMinWidth(150);
        button.setMinHeight(50);
        button.setEffect(new DropShadow(10, Color.BLACK));

        // Cập nhật hover hiệu ứng để bỏ border-radius
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #e0e0e0; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 0; -fx-background-radius: 0;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 15; -fx-background-radius: 15;"
        ));
    }
}
