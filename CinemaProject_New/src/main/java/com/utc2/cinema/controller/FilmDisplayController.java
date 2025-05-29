package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.service.FilmService;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class FilmDisplayController
{

    private final FilmService filmService = new FilmService();

    public void setupFilms() {
        List<Film> films = filmService.getAllFilms();
        showFilms(films);
    }
    @FXML private Pane ShowFilmDetail;
    @FXML private Pane buyForm;
    private ScrollPane mainShowFilm;
    private HBox moviePosters;
    private FlowPane moviePosters1;
    private Label filmNameLabel;
    private Label filmDirectorLabel;
    private Label filmActorLabel ;
    private Label filmReleaseDateLabel;
    private Label filmLengthLabel;
    private Label filmAgeLimitLabel;
    private Label filmContentLabel;
    private ImageView filmPosterImageView;
    private WebView webView;
    private Film selectedFilm;
    @FXML
    private TextField searchField;
    @FXML
    private Pane movieForm;
    @FXML Pane mainMenuForm;
    @FXML Pane showfilmdetail;
    @FXML
    private Pane scheduleForm;
    @FXML
    private Pane introForm;

    private BuyTicketController buyTicketController;
    public FilmDisplayController(MainMenuController mainMenu) {
        buyTicketController = mainMenu.getBuyTicketController();
        this.mainShowFilm = mainMenu.getMainShowFilm();
        this.buyForm = mainMenu.getBuyForm();
        this.ShowFilmDetail = mainMenu.getShowfilmdetail();
        this.moviePosters = mainMenu.getMoviePosters();
        this.moviePosters1 = mainMenu.getMoviePosters1();
        this.filmNameLabel = mainMenu.getFilmNameLabel();
        this.filmDirectorLabel = mainMenu.getFilmDirectorLabel();
        this.filmActorLabel = mainMenu.getFilmActorLabel();
        this.filmReleaseDateLabel = mainMenu.getFilmReleaseDateLabel();
        this.filmLengthLabel = mainMenu.getFilmLengthLabel();
        this.filmAgeLimitLabel = mainMenu.getFilmAgeLimitLabel();
        this.filmContentLabel = mainMenu.getFilmContentLabel();
        this.filmPosterImageView = mainMenu.getFilmPosterImageView();
        this.selectedFilm= mainMenu.getSelectedFilm();
        this.webView = mainMenu.getWebView();
        this.introForm=mainMenu.getIntroForm();
        this.mainMenuForm=mainMenu.getMainMenuForm();
        this.scheduleForm=mainMenu.getScheduleForm();
        this.movieForm=mainMenu.getMovieForm();
        this.searchField=mainMenu.getSearchField();
    }
    private HBox createMainItem(Film film) {
        String posterPath = "src/main/resources/Image/" + film.getPosterUrl() + ".png";
        File file = new File(posterPath);
        if (!file.exists()) {
            System.out.println("Không tìm thấy ảnh: " + posterPath);
            return null;
        }

        // Ảnh phim bên trái
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(120);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.setStyle("-fx-background-radius: 10 0 0 10;");

        // Thông tin phim bên phải
        Label nameLabel = new Label(film.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Label directorLabel = new Label("Đạo diễn: " + film.getDirector());
        directorLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 13px;");

        Button bookButton = new Button("Đặt vé");
        bookButton.setStyle(
                "-fx-background-color: #61C17E;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 5;"
        );
        bookButton.setOnAction(event -> {
            selectedFilm = film;
            setFilmDetails(film);
            ShowFilmDetail.setVisible(true);
        });

        VBox infoBox = new VBox(8, nameLabel, directorLabel, bookButton);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(10));
        infoBox.setPrefWidth(260); // phần thông tin rộng hơn để tổng vừa 400px

        HBox filmBox = new HBox(imageView, infoBox);
        filmBox.setSpacing(12);
        filmBox.setPadding(new Insets(10));
        filmBox.setPrefWidth(360);
        filmBox.setMaxWidth(360);
        filmBox.setMinWidth(360);


        filmBox.setStyle(
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.2, 0, 2);"
        );
        String[] arr = {"#8181AB", "#D6957B", "#907EC2","#566ED9","#B55959"};
        int randomIndex = (int)(Math.random() * arr.length);
        filmBox.setStyle("-fx-background-color: " + arr[randomIndex] + ";");

        TranslateTransition hoverUp = new TranslateTransition(Duration.millis(200), filmBox);
        hoverUp.setToY(-10);  // Dịch lên 10px so với vị trí gốc

        TranslateTransition hoverDown = new TranslateTransition(Duration.millis(200), filmBox);
        hoverDown.setToY(0);  // Trả về vị trí ban đầu (0)

        filmBox.setOnMouseEntered(e -> {
            hoverDown.stop();
            hoverUp.playFromStart();
        });

        filmBox.setOnMouseExited(e -> {
            hoverUp.stop();
            hoverDown.playFromStart();
        });



        return filmBox;
    }


    private VBox createFilmBox(Film film) {
        String posterPath = "src/main/resources/Image/" + film.getPosterUrl() + ".png"; // hoặc đường dẫn tương đối khác
        File file = new File(posterPath);
        if (!file.exists()) {
            System.out.println("Không tìm thấy ảnh: " + posterPath);
            return null;
        }
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(160);
        imageView.setFitHeight(190);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        Label nameLabel = new Label(film.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
        Label directorLabel = new Label("Đạo diễn: " + film.getDirector());
        directorLabel.setStyle("-fx-text-fill: black;");
        Button bookButton = new Button("🎟️ Đặt vé");
        bookButton.setStyle("-fx-background-color: #61C17E; -fx-text-fill: white;");
        bookButton.setOnAction(event -> {
            selectedFilm = film;
            System.out.println("Hiển thị thông tin chi tiết cho phim: " + film.getName());
            setFilmDetails(film);

            // Hiển thị form chi tiết phim (showFilmDetail)
            ShowFilmDetail.setVisible(true);
        });

        VBox filmBox = new VBox(8, imageView, nameLabel, directorLabel, bookButton);
        filmBox.setAlignment(Pos.CENTER);
        filmBox.setPadding(new Insets(10));
        filmBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #61C17E; -fx-border-radius: 3; -fx-background-radius: 3;");
        filmBox.setPrefWidth(180);
        FlowPane.setMargin(filmBox, new Insets(10, 12, 10,12));
        return filmBox;
    }


    private void showFilms(List<Film> films) {
        int count = 0;
        for (Film film : films) {
            try {
                HBox filmBox1 = createMainItem(film);
                VBox filmBox2 = createFilmBox(film);

                if (filmBox2 != null) moviePosters1.getChildren().add(filmBox2);
                if (count <= 3)
                {
                    if (filmBox1 != null) moviePosters.getChildren().add(filmBox1);
                    count++;
                }
                moviePosters.setSpacing(20);
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
        String posterPath = "src/main/resources/Image/" + film.getPosterUrl() + ".png"; // Ví dụ: "inception"
        File file = new File(posterPath);
        if (!file.exists()) {
            System.out.println("Không tìm thấy ảnh: " + posterPath);
            return;
        }
        Image image = new Image(file.toURI().toString());
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


    void handleBookTicket() {
        // Xử lý đặt vé ở đây
        if (selectedFilm != null) {
            // Nếu film đã được chọn, thực hiện hành động
            ShowFilmDetail.setVisible(false);
            buyForm.setVisible(true);
        } else {
            // Nếu không có phim được chọn, có thể hiển thị thông báo hoặc xử lý gì đó
            System.out.println("Không có phim nào được chọn!");
        }
    }
    void hideForm(){
        movieForm.setVisible(false);
        ShowFilmDetail.setVisible(false);
        scheduleForm.setVisible(false);
        mainMenuForm.setVisible(false);
        introForm.setVisible(false);
        buyForm.setVisible(false);
    }
    @FXML
    void onSearchFilmByName() {
        String keyword = searchField.getText().trim().toLowerCase();

        // Ẩn tất cả các pane không liên quan
        hideForm();

        // Hiện pane chứa danh sách film
        movieForm.setVisible(true);

        // Nếu ô tìm kiếm rỗng thì hiển thị tất cả film
        if (keyword.isEmpty()) {
            moviePosters.getChildren().clear();
            moviePosters1.getChildren().clear();
            setupFilms();
            return;
        }

        // Lọc danh sách phim
        List<Film> filtered = filmService.getAllFilms().stream()
                .filter(f -> f.getName().toLowerCase().contains(keyword))
                .toList();

        // Hiển thị phim đã lọc
        moviePosters.getChildren().clear();
        moviePosters1.getChildren().clear();
        showFilms(filtered);
    }

}
