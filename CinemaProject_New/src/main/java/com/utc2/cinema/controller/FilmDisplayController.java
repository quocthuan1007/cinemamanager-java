package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.service.FilmService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
    private FlowPane moviePosters;
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

    @FXML private BuyTicketController buyTicketController;
    public FilmDisplayController(MainMenuController mainMenu) {
        this.buyTicketController = mainMenu.getBuyTicketController();
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

    //
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
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label directorLabel = new Label("Đạo diễn: " + film.getDirector());

        Button bookButton = new Button("🎟️ Đặt vé");
        bookButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");
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
        filmBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");
        filmBox.setPrefWidth(180);
        FlowPane.setMargin(filmBox, new Insets(10, 12, 10,12));
        return filmBox;
    }

    private void showFilms(List<Film> films) {
        int count = 0;
        for (Film film : films) {
            try {
                VBox filmBox1 = createFilmBox(film);
                VBox filmBox2 = createFilmBox(film);

                if (filmBox2 != null) moviePosters1.getChildren().add(filmBox2);
                if (count <= 3)
                {
                    if (filmBox1 != null) moviePosters.getChildren().add(filmBox1);
                    count++;
                }
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


    void handleBookTicket() {
        // Xử lý đặt vé ở đây
        if (selectedFilm != null) {
            // Nếu film đã được chọn, thực hiện hành động
            ShowFilmDetail.setVisible(false);
            buyForm.setVisible(true);
//            buyTicketController.showMovieShowOfFilm(selectedFilm.getId());
             // Sử dụng ID của film
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
