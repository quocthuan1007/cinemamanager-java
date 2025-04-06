package com.utc2.cinema.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.service.FilmService;

import java.io.InputStream;
import java.util.List;

public class MainController {

    @FXML private Text homeText, moviesText, scheduleText, ticketText, aboutText;
    @FXML private TextField searchBox;
    @FXML private Button loginBtn, registerBtn;
    @FXML private ImageView mainPoster;
    @FXML private HBox movieList;

    @FXML
    public void initialize() {
        setMenuEvents();
        loadMainPoster();
        loadMovies();
    }

    private void setMenuEvents() {
        homeText.setOnMouseClicked(e -> System.out.println("Trang Chủ được chọn!"));
        moviesText.setOnMouseClicked(e -> System.out.println("Phim được chọn!"));
        // Các mục khác tương tự
    }

    private void loadMainPoster() {
        Image poster = new Image(getClass().getResource("/Image/poster/posterNhaGiaTien1.png").toExternalForm());
        mainPoster.setImage(poster);
    }

    private void loadMovies() {
        FilmService filmService = new FilmService();
        List<Film> films = filmService.getAllFilms();
        int count = 0;

        for (Film film : films) {
            if (count >= 4) break;

            try {
                String posterUrl = film.getPosterUrl();
                if (!posterUrl.endsWith(".png")) posterUrl += ".png";

                InputStream is = getClass().getResourceAsStream("/Image/" + posterUrl);
                if (is == null) continue;

                Image poster = new Image(is);
                ImageView imageView = new ImageView(poster);
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true);

                Label name = new Label(film.getName());
                name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

                Label director = new Label("Đạo diễn: " + film.getDirector());
                director.setStyle("-fx-text-fill: #666;");

                Button bookBtn = new Button("🎟️ Đặt vé");
                bookBtn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-background-radius: 6;");
                bookBtn.setOnAction(e -> System.out.println("Đặt vé: " + film.getName()));

                VBox box = new VBox(8, imageView, name, director, bookBtn);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(10));
                box.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 12; -fx-background-radius: 12;");

                movieList.getChildren().add(box);
                count++;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
