package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Film;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShowFilmDetailController {

    @FXML private Label filmNameLabel;
    @FXML private Label filmDirectorLabel;
    @FXML private Label filmActorLabel;
    @FXML private Label filmTypeLabel;
    @FXML private Label filmReleaseDateLabel;
    @FXML private Label filmLengthLabel;
    @FXML private Label filmAgeLimitLabel;
    @FXML private Label filmContentLabel;
    @FXML private ImageView filmPosterImageView;

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
    }
}
