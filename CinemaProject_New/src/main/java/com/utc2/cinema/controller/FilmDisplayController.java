package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.service.FilmService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.InputStream;
import java.util.List;

public class FilmDisplayController
{
    private MainMenuController mainMenu = new MainMenuController();
    private final FilmService filmService = new FilmService();
    public void setupFilms() {
        List<Film> films = filmService.getAllFilms();
        showFilms(films);
    }
    private Pane ShowFilmDetail;
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
    public FilmDisplayController(MainMenuController mainMenu) {
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
        this.webView = mainMenu.getWebView();
    }
    //
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
            ShowFilmDetail.setVisible(true);
        });

        VBox filmBox = new VBox(8, imageView, nameLabel, directorLabel, bookButton);
        filmBox.setAlignment(Pos.CENTER);
        filmBox.setPadding(new Insets(10));
        filmBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");
        filmBox.setPrefWidth(180);
        FlowPane.setMargin(filmBox, new Insets(10, 20, 10,0));
        return filmBox;
    }

    private void showFilms(List<Film> films) {
        int count = 0;
        for (Film film : films) {
            try {
                VBox filmBox1 = createFilmBox(film);
                VBox filmBox2 = createFilmBox(film);

                if (filmBox2 != null) moviePosters1.getChildren().add(filmBox2);
                if (count <= 2)
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
}
