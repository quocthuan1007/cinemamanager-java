package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Film;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

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
    @FXML private WebView webView; // WebView để hiển thị trailer

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

        String embedHTML = """
            <html>
                <body style='margin:0px;padding:0px;'>
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
@FXML
private void handleWatchTrailer(ActionEvent event) {
    // Tạo Stage mới
    Stage trailerStage = new Stage();
    trailerStage.setTitle("Trailer");

    // Tạo WebView
    WebView webView = new WebView();
    webView.setPrefSize(800, 450); // Tỷ lệ 16:9
    webView.getEngine().load(trailerUrl); // Bạn sẽ gán biến trailerUrl từ dữ liệu phim

    // Đưa WebView vào Scene
    StackPane root = new StackPane(webView);
    Scene scene = new Scene(root);
    trailerStage.setScene(scene);
    trailerStage.show();
}
