package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.dao.MovieShowDao;
import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.model.entity.MovieShow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScheduleController implements Initializable {

    @FXML
    private FlowPane dateFlowPane;      // container cho các nút chọn ngày
    @FXML
    private VBox scheduleContainer;     // container để add phim + suất

    private final FilmDao filmDao = new FilmDao();
    private final MovieShowDao movieShowDao = new MovieShowDao();
    private final DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("dd/MM");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Sinh nút cho 7 ngày từ hôm nay
        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            Button btn = new Button(dayFmt.format(date));
            btn.setOnAction(e -> loadSchedule(date));
            btn.setPrefWidth(80);
            dateFlowPane.getChildren().add(btn);
        }
        // 2. Mặc định show hôm nay
        loadSchedule(LocalDate.now());
    }

    private void loadSchedule(LocalDate date) {
        scheduleContainer.getChildren().clear();

        // Lấy map phim -> list suất
        Map<Film, List<MovieShow>> map = new LinkedHashMap<>();
        for (Film f : filmDao.getAllFilms()) {
            List<MovieShow> movieShows = movieShowDao.getShowByFilmIdAndDate(f.getId(), date);
            if (!movieShows.isEmpty()) map.put(f, movieShows);
        }

        // Build UI cho từng phim
        for (var entry : map.entrySet()) {
            Film film = entry.getKey();
            List<MovieShow> movieShows = entry.getValue();

            HBox filmBox = new HBox(10);
            filmBox.setPadding(new Insets(8));
            filmBox.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 2;");

            // Poster
            ImageView poster = new ImageView(new Image("file:" + film.getPosterUrl()));
            poster.setFitWidth(100);
            poster.setPreserveRatio(true);

            // Tên phim
            Label lblName = new Label(film.getName());
            lblName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            // Các button giờ chiếu
            HBox times = new HBox(5);
            for (MovieShow s : movieShows) {
                String timeStr = s.getStartTime().toLocalTime().toString().substring(0,5)
                        + " - " + s.getEndTime().toLocalTime().toString().substring(0,5);
                Button tbtn = new Button(timeStr);
                tbtn.setOnAction(evt -> {
                    // ví dụ: mở popup đặt vé, hoặc điều hướng trang mua vé
                });
                times.getChildren().add(tbtn);
            }

            VBox info = new VBox(4, lblName, times);
            filmBox.getChildren().addAll(poster, info);
            scheduleContainer.getChildren().add(filmBox);
        }
    }
}
