package com.utc2.cinema.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.utc2.cinema.service.FilmService;
import com.utc2.cinema.model.entity.Film;

import java.io.InputStream;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Tạo menu items
        Text home = createMenuItem("Trang Chủ");
        Text movies = createMenuItem("Phim");
        Text schedule = createMenuItem("Lịch Chiếu");
        Text ticket = createMenuItem("Mua Vé");
        Text about = createMenuItem("Giới Thiệu");

        // Ô tìm kiếm
        TextField searchBox = new TextField();
        searchBox.setPromptText("Nhập tên phim");
        searchBox.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-padding: 5;");

        // Nút Đăng nhập và Đăng ký
        Button loginBtn = createButton("Đăng Nhập");
        Button registerBtn = createButton("Đăng Ký");

        // Thanh menu
        HBox menuBar = new HBox(15, home, movies, schedule, ticket, about, searchBox, loginBtn, registerBtn);
        menuBar.setAlignment(Pos.CENTER);
        menuBar.setPadding(new Insets(10));
        menuBar.setStyle("-fx-background-color: #444;");

        // 📌 Thêm poster chính
        Image posterImage = new Image(getClass().getResource("/Image/poster/posterNhaGiaTien1.png").toExternalForm());
        // Đường dẫn ảnh
        ImageView posterView = new ImageView(posterImage);
        posterView.setFitWidth(800);  // Đặt chiều rộng
        posterView.setPreserveRatio(true); // Giữ tỷ lệ ảnh

        // Bọc poster trong StackPane để căn giữa
        StackPane posterPane = new StackPane(posterView);
        VBox.setMargin(posterPane, new Insets(20, 0, 0, 0));
        posterPane.setAlignment(Pos.TOP_CENTER); // Căn trên và giữa ngang

        // Lấy dữ liệu phim từ cơ sở dữ liệu
        FilmService filmService = new FilmService();
        List<Film> films = filmService.getAllFilms();

        // Dựng grid cho các poster phim dưới poster chính
        HBox moviePosters = new HBox(20);  // Khoảng cách giữa các poster
        moviePosters.setAlignment(Pos.CENTER);
        moviePosters.setPadding(new Insets(20, 0, 0, 0));

        int count = 0;
        for (Film film : films) {
            if (count >= 3) break;

            try {
                String posterUrl = film.getPosterUrl();
                if (!posterUrl.endsWith(".png")) {
                    posterUrl += ".png";
                }
                String posterPath = "/Image/" + posterUrl;

                InputStream is = getClass().getResourceAsStream(posterPath);
                if (is == null) {
                    System.out.println("Không tìm thấy ảnh: " + posterPath);
                    continue;
                }

                Image moviePosterImage = new Image(is);
                ImageView moviePosterView = new ImageView(moviePosterImage);
                moviePosterView.setFitWidth(150);
                moviePosterView.setPreserveRatio(true);
                moviePosterView.setSmooth(true);

                // Tên phim (bold, size lớn hơn)
                Label nameLabel = new Label(film.getName());
                nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

                // Tên đạo diễn
                Label directorLabel = new Label("Đạo diễn: " + film.getDirector());
                directorLabel.setStyle("-fx-text-fill: #666;");

                // Nút đặt vé
                Button bookButton = new Button("🎟️ Đặt vé");
                bookButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-background-radius: 6;");
                bookButton.setOnMouseEntered(e -> bookButton.setStyle("-fx-background-color: #005999; -fx-text-fill: white; -fx-background-radius: 6;"));
                bookButton.setOnMouseExited(e -> bookButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-background-radius: 6;"));

                bookButton.setOnAction(event -> {
                    System.out.println("Đặt vé cho phim: " + film.getName());
                    // TODO: mở giao diện đặt vé
                });

                // Gói các thành phần vào VBox
                VBox filmBox = new VBox(8);
                filmBox.getChildren().addAll(moviePosterView, nameLabel, directorLabel, bookButton);
                filmBox.setAlignment(Pos.CENTER);
                filmBox.setPadding(new Insets(15));
                filmBox.setPrefWidth(180);
                filmBox.setStyle(
                        "-fx-background-color: #f9f9f9;" +
                                "-fx-background-radius: 12;" +
                                "-fx-border-color: #ddd;" +
                                "-fx-border-radius: 12;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.5, 0, 2);"
                );

                moviePosters.getChildren().add(filmBox);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }









        // Dùng VBox để chứa menu, poster chính và các poster phim
        VBox contentBox = new VBox(menuBar, posterPane, moviePosters);
        contentBox.setSpacing(0);
        contentBox.setPadding(new Insets(0));

        // Đặt content vào BorderPane
        BorderPane root = new BorderPane();
        root.setTop(contentBox);

        // Giao diện chính
        Scene scene = new Scene(root, 1160, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quản Lý Rạp Chiếu Phim");
        primaryStage.show();
    }

    // Hàm tạo Text menu item với hiệu ứng
    private Text createMenuItem(String name) {
        Text text = new Text(name);
        text.setFill(Color.WHITE);
        text.setFont(Font.font(14));

        // Hiệu ứng hover
        text.setOnMouseEntered(e -> text.setFill(Color.ORANGE));
        text.setOnMouseExited(e -> text.setFill(Color.WHITE));

        // Sự kiện click
        text.setOnMouseClicked(e -> System.out.println(name + " đã được chọn!"));

        return text;
    }

    // Hàm tạo button
    private Button createButton(String name) {
        Button btn = new Button(name);
        btn.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #e65c00; -fx-text-fill: white;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #444; -fx-text-fill: white;"));
        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
