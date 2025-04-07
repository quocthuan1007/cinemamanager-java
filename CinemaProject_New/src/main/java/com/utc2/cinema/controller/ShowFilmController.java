package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.model.entity.UserSession;
import com.utc2.cinema.service.FilmService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ShowFilmController implements Initializable {

    @FXML
    private Button buyBtn;
    @FXML
    private Label userMain;

    @FXML
    private Pane buyForm;

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
    private ImageView posterImage;

    @FXML
    private Button scheduleBtn;

    @FXML
    private Pane scheduleForm;
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

    private void showFilms(List<Film> films) {
        int count = 0;
        for (Film film : films) {
            if (count >= 8) break; // Giới hạn số lượng phim hiển thị

            try {
                // Lấy đường dẫn ảnh từ cơ sở dữ liệu và tạo đối tượng Image
                String posterPath = "/Image/" + film.getPosterUrl()+".png"; // Đảm bảo tên ảnh từ database không có phần mở rộng

                // Thêm dấu "/" phía trước để tạo đúng đường dẫn trong resources
                InputStream is = getClass().getResourceAsStream(posterPath);
                if (is == null) {
                    System.out.println("Không tìm thấy ảnh: " + posterPath);
                    continue;
                }

                Image image = new Image(is);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(160);
                imageView.setFitHeight(190);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                // Hiển thị tên phim và đạo diễn
                Label nameLabel = new Label(film.getName());
                nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                Label directorLabel = new Label("Đạo diễn: " + film.getDirector());

                // Nút đặt vé
                Button bookButton = new Button("🎟️ Đặt vé");
                bookButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");
                bookButton.setOnAction(event -> {
                    System.out.println("Hiển thị thông tin chi tiết cho phim: " + film.getName());
                    openFilmDetailWindow(film); // Mở cửa sổ chi tiết bộ phim
                });

                // Tạo VBox chứa ảnh và thông tin phim
                VBox filmBox = new VBox(8, imageView, nameLabel, directorLabel, bookButton);
                filmBox.setAlignment(Pos.CENTER);
                filmBox.setPadding(new Insets(10));
                filmBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");
                filmBox.setPrefWidth(180);

                moviePosters.getChildren().add(filmBox);
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

}
