package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.dao.MovieShowDao;
import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.model.entity.MovieShow;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleDisplayController
{
    private VBox scheduleContainer;
    private FlowPane dateFlowPane;
    private Pane scheduleForm;
    private Pane buyForm;
    private BuyTicketController buyTicketController;
    public ScheduleDisplayController(MainMenuController mainMenu)
    {
        this.buyTicketController = mainMenu.getBuyTicketController();
        this.scheduleContainer = mainMenu.getScheduleContainer();
        this.dateFlowPane = mainMenu.getDateFlowPane();
        this.scheduleForm = mainMenu.getScheduleForm();
        this.buyForm = mainMenu.getBuyForm();
    }
    private final MovieShowDao movieShowDao = new MovieShowDao();
    private final FilmDao filmDao = new FilmDao();
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
            String posterPath = "src/main/resources/Image/" + film.getPosterUrl() + ".png"; // hoặc đường dẫn tương đối khác
            File file = new File(posterPath);
            if (!file.exists()) {
                System.out.println("Không tìm thấy ảnh: " + posterPath);
                return;
            }
            Image image = new Image(file.toURI().toString());

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(160);
            imageView.setFitHeight(190);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            // Tạo VBox chứa tên phim trên poster
            VBox filmNameBox = new VBox(5);
            Label filmNameLabel = new Label(film.getName());
            filmNameLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
            filmNameBox.getChildren().add(filmNameLabel);

            // Tạo HBox chứa poster và tên phim (tên phim trên cùng, poster dưới)
            VBox posterBox = new VBox(10); // spacing = 10px giữa các phần tử con
            posterBox.setAlignment(Pos.TOP_CENTER); // Căn giữa theo chiều ngang, trên cùng theo chiều dọc
            posterBox.setStyle("-fx-padding: 10px;"); // Thêm padding nếu cần
            posterBox.getChildren().addAll(filmNameBox, imageView); // Poster dưới tên phim

            // Tạo VBox để chứa lịch chiếu của bộ phim
            VBox scheduleBox = new VBox(10); // spacing = 10px giữa các phần tử con
            scheduleBox.setAlignment(Pos.TOP_LEFT); // Căn trái, trên cùng

            // Thêm lịch chiếu vào VBox
            for (MovieShow movieShowDetails : showsForFilm) {
                String showStart = movieShowDetails.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                String showEnd = movieShowDetails.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));

                String showDetails = "Bắt đầu: " + showStart + " | Kết thúc: " + showEnd;
                Label showLabel = new Label(showDetails);
                showLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px; " +
                        "-fx-background-color: transparent; -fx-border-color: #32CD32; " +
                        "-fx-border-radius: 8px; -fx-border-width: 2; " +
                        "-fx-text-fill: black;");

                scheduleBox.getChildren().add(showLabel);
            }

            // Tạo HBox để chứa cả poster và lịch chiếu
            HBox movieBox = new HBox(20); // spacing = 20px giữa posterBox và scheduleBox
            movieBox.setAlignment(Pos.TOP_CENTER); // Căn giữa theo chiều ngang, trên cùng theo chiều dọc
            movieBox.getChildren().addAll(posterBox, scheduleBox);
            movieBox.setOnMouseEntered(e ->
            {
                movieBox.setStyle(
                                "-fx-background-color: #cccccc; "
                );
            });
            movieBox.setOnMouseClicked(e ->
            {
                scheduleForm.setVisible(false);
                buyForm.setVisible(true);
                HBox filmSave= buyTicketController.getFilmSave();
                Map<Integer,HBox> filmHBoxMap = buyTicketController.getFilmBoxMap();
                if (filmSave != null)
                    filmSave.setStyle("-fx-alignment: center; -fx-padding: 10px; -fx-border-color: black; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color: transparent;");

                filmSave = filmHBoxMap.get(filmId);
                if (filmSave != null){
                    buyTicketController.setFilmSave(filmSave);
                    filmSave.setStyle("-fx-alignment: center; -fx-padding: 10px; -fx-border-color: black; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color: green;");
                }
                buyTicketController.showMovieShowOfFilm(filmId);
            });

            movieBox.setOnMouseExited(e -> {
                movieBox.setStyle(
                        "-fx-background-color: transparent; "  // Trở lại viền trong suốt khi không hover
                );
            });
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
    public void setupSchedule() {
        LocalDate today = LocalDate.now();

        // Hiển thị lịch hôm nay
        showScheduleForDate(today);

        // Tạo nút 7 ngày tới
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = today.plusDays(i);
            Button dateButton = new Button(currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dateButton.setOnAction(e -> handleDateButtonClick(currentDate));
            styleButton(dateButton);
            dateFlowPane.getChildren().add(dateButton);
        }
    }
}
