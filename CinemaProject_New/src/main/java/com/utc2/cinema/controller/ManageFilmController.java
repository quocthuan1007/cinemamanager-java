package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.model.entity.Film;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class ManageFilmController {
    @FXML
    private TextField CountryFilm;

    @FXML
    private TextField actorFilm;

    @FXML
    private TableColumn<?, ?> actorFilmCol;

    @FXML
    private Button adPosterBtn;

    @FXML
    private Button addFilmBtn;

    @FXML
    private TextField ageFilm;

    @FXML
    private TextArea conFilm;

    @FXML
    private TableColumn<?, ?> countryFilmCol;

    @FXML
    private Button deleteFilmBtn;

    @FXML
    private TextField direFilm;

    @FXML
    private Button editFilmBtn;

    @FXML
    private TextField lenFilm;

    @FXML
    private TextField nameFilm;

    @FXML
    private TableColumn<?, ?> nameFilmCol;

    @FXML
    private Button posterBtn;

    @FXML
    private DatePicker releaseFilm;

    @FXML
    private TextField searchFilm;

    @FXML
    private TextField staFilm;

    @FXML
    private TableColumn<?, ?> statusFilmCol;

    @FXML
    private TableView<?> tableFilm;

    @FXML
    private TextField trailerFilm;

    @FXML
    private Button viewBtn11;
    private String adPosterPath = "";
    private String posterPath = "";

    // Copy ảnh và trả về đường dẫn tương đối
    private String copyImageToFolder(File sourceFile, String folderName) throws IOException {
        // Lấy phần mở rộng của file
        String fileExtension = sourceFile.getName().substring(sourceFile.getName().lastIndexOf('.'));

        // Tên file mới, có thể thêm UUID tránh trùng tên
        String fileNameWithoutExtension = UUID.randomUUID() + "_" + sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf('.'));

        // Thư mục đích
        File destDir = new File("src/main/resources/Image/" + folderName);
        if (!destDir.exists()) destDir.mkdirs();

        // File đích
        File destFile = new File(destDir, fileNameWithoutExtension + fileExtension); // Lưu file với đuôi mở rộng

        // Sao chép file vào thư mục đích
        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Trả về đường dẫn tương đối mà không có phần mở rộng
        return folderName + "/" + fileNameWithoutExtension; // Trả về tên file không đuôi
    }

    @FXML
    void onClickAddFilm(ActionEvent event) {
        try {
            // Lấy dữ liệu từ các input
            String name = nameFilm.getText();
            String country = CountryFilm.getText();
            int length = Integer.parseInt(lenFilm.getText());
            String director = direFilm.getText();
            String actor = actorFilm.getText();
            int ageLimit = Integer.parseInt(ageFilm.getText());
            String status = staFilm.getText();
            String trailer = trailerFilm.getText();
            String content = conFilm.getText();
            LocalDate releaseDateValue = releaseFilm.getValue();

            if (releaseDateValue == null) {
                System.out.println("Chưa chọn ngày phát hành!");
                return;
            }
            LocalDate localDate = releaseFilm.getValue();

            // Chuyển LocalDate sang java.util.Date
            Date releaseDate = null;
            if (localDate != null) {
                releaseDate = java.sql.Timestamp.valueOf(localDate.atStartOfDay());
            }

            Film film = new Film(0, name, country, length, director, actor, ageLimit,
                    status, trailer, content, adPosterPath, posterPath, releaseDate);     // từ onClickPoster

            // Gọi insert
            boolean success = FilmDao.insertFilm(film);
            if (success) {
                System.out.println("Thêm phim thành công!");
                // TODO: reload table, clear fields nếu cần
            } else {
                System.out.println("Thêm phim thất bại.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Lỗi định dạng số!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickDeleteFilm(ActionEvent event) {

    }

    @FXML
    void onClickEditFilm(ActionEvent event) {

    }

    @FXML
    void onClickAdPoster(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh Poster quảng cáo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                adPosterPath = copyImageToFolder(selectedFile, "poster");
                adPosterBtn.setText("Đã chọn");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onClickPoster(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh Poster chính");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Lấy tên file mà không có phần mở rộng
                String fileNameWithoutExtension = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.'));
                // Thêm đường dẫn mới (bỏ đuôi file)
                posterPath = copyImageToFolder(selectedFile, "poster/" + fileNameWithoutExtension);
                posterBtn.setText("Đã chọn");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void onClickViewFilm(ActionEvent event) {

    }

    @FXML
    void onSearchFilm(ActionEvent event) {

    }
}
