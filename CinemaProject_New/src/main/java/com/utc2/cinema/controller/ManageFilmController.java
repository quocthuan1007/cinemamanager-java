package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.model.entity.CustomAlert;
import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.model.entity.UserAccount;
import com.utc2.cinema.service.FilmService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ManageFilmController {
    private TextField CountryFilm;
    private TextField actorFilm;
    private TableColumn<Film, String> actorFilmCol;
    private Button adPosterBtn;
    private Button addFilmBtn;
    private TextField ageFilm;
    private TextArea conFilm;
    private TableColumn<Film, String> countryFilmCol;
    private Button deleteFilmBtn;
    private TextField direFilm;
    private Button editFilmBtn;
    private TextField lenFilm;
    private TextField nameFilm;
    private TableColumn<Film, String> nameFilmCol;
    private Button posterBtn;
    private DatePicker releaseFilm;
    private TextField searchFilm;
    private TextField staFilm;
    private TableColumn<Film, String> statusFilmCol;
    private TableView<Film> tableFilm;
    private TextField trailerFilm;
    private Button viewBtn11;
    private String adPosterPath;
    private String posterPath;
    private Button addOrSaveBtn;
    private ImageView adPosterImg;
    private ImageView posterImg;
    private Pane filmForm;
    private ObservableList<Film> filmList = FXCollections.observableArrayList();

    public ManageFilmController(MainManagerController mainMenu) {
        this.CountryFilm = mainMenu.getCountryFilm();
        this.actorFilm = mainMenu.getActorFilm();
        this.actorFilmCol = mainMenu.getActorFilmCol();
        this.adPosterBtn = mainMenu.getAdPosterBtn();
        this.addFilmBtn = mainMenu.getAddFilmBtn();
        this.ageFilm = mainMenu.getAgeFilm();
        this.conFilm = mainMenu.getConFilm();
        this.countryFilmCol = mainMenu.getCountryFilmCol();
        this.deleteFilmBtn = mainMenu.getDeleteFilmBtn();
        this.direFilm = mainMenu.getDireFilm();
        this.editFilmBtn = mainMenu.getEditFilmBtn();
        this.lenFilm = mainMenu.getLenFilm();
        this.nameFilm = mainMenu.getNameFilm();
        this.nameFilmCol = mainMenu.getNameFilmCol();
        this.releaseFilm = mainMenu.getReleaseFilm();
        this.searchFilm = mainMenu.getSearchFilm();
        this.staFilm = mainMenu.getStaFilm();
        this.statusFilmCol = mainMenu.getStatusFilmCol();
        this.tableFilm = mainMenu.getTableFilm();
        this.trailerFilm = mainMenu.getTrailerFilm();
        this.adPosterPath = mainMenu.getAdPosterPath();
        this.posterPath = mainMenu.getPosterPath();
        this.posterBtn = mainMenu.getPosterBtn();
        this.addOrSaveBtn = mainMenu.getAddOrSaveBtn();
        this.posterImg = mainMenu.getPosterImg();
        this.adPosterImg = mainMenu.getAdPosterImg();
        this.filmForm = mainMenu.getFilmForm();
    }
    public void init()
    {
        nameFilmCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        countryFilmCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCountry()));
        actorFilmCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDirector()));
        statusFilmCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFilmStatus()));
        filmList.setAll(getDataFilm());
        tableFilm.setItems(filmList);
        tableFilm.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {  // Kiểm tra xem có phải double click không
                Film selectedFilm = tableFilm.getSelectionModel().getSelectedItem();
                if (selectedFilm != null) {
                    onClickEditFilm(new ActionEvent());
                    System.out.println("Double click vào phim: " + selectedFilm.getName());
                }
            }
        });
    }
    private void clearFilmForm() {
        nameFilm.clear();
        CountryFilm.clear();
        lenFilm.clear();
        direFilm.clear();
        actorFilm.clear();
        ageFilm.clear();
        staFilm.clear();
        trailerFilm.clear();
        conFilm.clear();
        releaseFilm.setValue(null);
        adPosterImg.setImage(null);
        posterImg.setImage(null);
        adPosterBtn.setText("Chọn");
        posterBtn.setText("Chọn");
        adPosterPath = null;
        posterPath = null;
    }

    ObservableList<Film>getDataFilm()
    {
        ObservableList<Film> listFilm = FXCollections.observableArrayList();
        FilmService query = new FilmService();
        List<Film>list = query.getAllFilms();
        for(Film film : list)
        {
            listFilm.add(film);
        }
        return listFilm;
    }
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

    public void onClickAddFilm(ActionEvent event)
    {
        if (!filmForm.isVisible()) filmForm.setVisible(true);
        addOrSaveBtn.setText("Thêm");
        clearFilmForm();
        addOrSaveBtn.setOnAction(eventVip -> {
            try {
                String name = nameFilm.getText().trim();
                String country = CountryFilm.getText().trim();
                String lenText = lenFilm.getText().trim();
                String director = direFilm.getText().trim();
                String actor = actorFilm.getText().trim();
                String ageText = ageFilm.getText().trim();
                String status = staFilm.getText().trim();
                String trailer = trailerFilm.getText().trim();
                String content = conFilm.getText().trim();
                LocalDate releaseDateValue = releaseFilm.getValue();

                // Kiểm tra thông tin bắt buộc
                if (name.isEmpty() || country.isEmpty() || lenText.isEmpty() || director.isEmpty()
                        || actor.isEmpty() || ageText.isEmpty() || status.isEmpty()
                        || trailer.isEmpty() || content.isEmpty() || releaseDateValue == null) {
                    CustomAlert.showError("Lỗi định dạng", "Có lỗi xảy ra", "Vui lòng nhập đầy đủ các trường bắt buộc.");
                    return;
                }
                int length, ageLimit;
                try {
                    length = Integer.parseInt(lenText);
                    ageLimit = Integer.parseInt(ageText);
                } catch (NumberFormatException e) {
                    CustomAlert.showError("Lỗi định dạng","Có lỗi xảy ra", "Độ dài phim và tuổi giới hạn phải là số.");
                    return;
                }
                if (name.length() > 100 || country.length() > 100 || director.length() > 100 || actor.length() > 100) {
                    CustomAlert.showError("Lỗi định dạng","Có lỗi xảy ra", "Tên, Quốc gia, Đạo diễn và Diễn viên không được vượt quá 100 ký tự.");
                    return;
                }

                if (trailer.length() > 500 || content.length() > 500) {
                    CustomAlert.showError("Lỗi định dạng","Có lỗi xảy ra", "Nội dung và Trailer không được vượt quá 500 ký tự.");
                    return;
                }

                if (length <= 0 || length > 500) {
                    CustomAlert.showError("Lỗi định dạng","Có lỗi xảy ra", "Độ dài phim phải từ 1 đến 500 phút.");
                    return;
                }

                if (ageLimit < 0 || ageLimit > 21) {
                    CustomAlert.showError("Lỗi định dạng","Có lỗi xảy ra", "Tuổi giới hạn phải từ 0 đến 21.");
                    return;
                }
                Date releaseDate = java.sql.Timestamp.valueOf(releaseDateValue.atStartOfDay());

                Film film = new Film(0, name, country, length, director, actor, ageLimit,
                        status, trailer, content, adPosterPath, posterPath, releaseDate);

                boolean success = FilmDao.insertFilm(film);
                if (success) {
                    CustomAlert.showInfo("Thành công", "Thêm phim", "Bạn vừa thêm phim thành công!");
                    clearFilmForm();
                    filmList.add(film);
                } else {
                    CustomAlert.showError("Lỗi hệ thống","Có lỗi xảy ra", "Thêm phim thất bại.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                CustomAlert.showError("Lỗi hệ thống","Có lỗi xảy ra", "Đã xảy ra lỗi: " + e.getMessage());
            }
        });
    }


    public void onClickDeleteFilm(ActionEvent event) {
        Film selectedFilm = tableFilm.getSelectionModel().getSelectedItem();
        if (selectedFilm == null) {
            System.out.println("Vui lòng chọn một phim để xóa.");
            return;
        }

        // Xác nhận xóa
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có chắc chắn muốn xóa phim '" + selectedFilm.getName() + "' không?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean deleted = FilmService.deleteFilm(selectedFilm.getId());
                if (deleted) {
                    filmList.remove(selectedFilm);
                    System.out.println("Đã xóa phim thành công.");
                } else {
                    System.out.println("Xóa phim thất bại.");
                }
                clearFilmForm();
                if(filmForm.isVisible()) filmForm.setVisible(false);
            }
        });
    }

    public void onClickEditFilm(ActionEvent event) {
        Film selectedFilm = tableFilm.getSelectionModel().getSelectedItem();

        if (selectedFilm != null) {
            if (!filmForm.isVisible()) filmForm.setVisible(true);
            nameFilm.setText(selectedFilm.getName());
            CountryFilm.setText(selectedFilm.getCountry());
            lenFilm.setText(String.valueOf(selectedFilm.getLength()));
            direFilm.setText(selectedFilm.getDirector());
            actorFilm.setText(selectedFilm.getActor());
            ageFilm.setText(String.valueOf(selectedFilm.getAgeLimit()));
            staFilm.setText(selectedFilm.getFilmStatus());
            trailerFilm.setText(selectedFilm.getTrailer());
            conFilm.setText(selectedFilm.getContent());
            releaseFilm.setValue(new java.sql.Date(selectedFilm.getReleaseDate().getTime()).toLocalDate());

            adPosterPath = selectedFilm.getAdPosterUrl();
            posterPath = selectedFilm.getPosterUrl();

            if (adPosterPath != null && !adPosterPath.isEmpty()) {
                File file = new File("src/main/resources/Image/" + adPosterPath + ".png");
                if (file.exists()) {
                    adPosterImg.setImage(new Image(file.toURI().toString()));
                }
            }

            if (posterPath != null && !posterPath.isEmpty()) {
                File file = new File("src/main/resources/Image/" + posterPath + ".png");
                if (file.exists()) {
                    posterImg.setImage(new Image(file.toURI().toString()));
                }
            }

            addOrSaveBtn.setText("Lưu chỉnh sửa");
            addOrSaveBtn.setOnAction(e -> {
                try {
                    String name = nameFilm.getText().trim();
                    String country = CountryFilm.getText().trim();
                    String lenText = lenFilm.getText().trim();
                    String director = direFilm.getText().trim();
                    String actor = actorFilm.getText().trim();
                    String ageText = ageFilm.getText().trim();
                    String status = staFilm.getText().trim();
                    String trailer = trailerFilm.getText().trim();
                    String content = conFilm.getText().trim();
                    LocalDate localDate = releaseFilm.getValue();
                    if (name.isEmpty() || country.isEmpty() || lenText.isEmpty() || director.isEmpty()
                            || actor.isEmpty() || ageText.isEmpty() || status.isEmpty()
                            || trailer.isEmpty() || content.isEmpty() || localDate == null) {
                        CustomAlert.showError("Lỗi định dạng", "Có lỗi xảy ra", "Vui lòng không được để trống!");
                        return;
                    }
                    int length, ageLimit;
                    try {
                        length = Integer.parseInt(lenText);
                        ageLimit = Integer.parseInt(ageText);
                    } catch (NumberFormatException ex) {
                        CustomAlert.showError("Lỗi định dạng", "Có lỗi xảy ra", "Độ dài phim là số!");
                        return;
                    }
                    if (name.length() > 100 || country.length() > 100 || director.length() > 100 || actor.length() > 100) {
                        CustomAlert.showError("Lỗi định dạng", "Có lỗi xảy ra", "Tên, Quốc gia, Đạo diễn và Diễn viên không được vượt quá 100 ký tự.");
                        return;
                    }
                    if (trailer.length() > 500 || content.length() > 500) {
                        CustomAlert.showError("Lỗi định dạng", "Có lỗi xảy ra", "Nội dung và Trailer không được vượt quá 500 ký tự.");
                        return;
                    }
                    if (length <= 0 || length > 500) {
                        CustomAlert.showError("Lỗi định dạng", "Có lỗi xảy ra", "Độ dài phim phải từ 1 đến 500 phút.");
                        return;
                    }
                    if (ageLimit < 0 || ageLimit > 21) {
                        CustomAlert.showError("Lỗi định dạng", "Có lỗi xảy ra", "Tuổi giới hạn phải từ 0 đến 21.");
                        return;
                    }
                    selectedFilm.setName(name);
                    selectedFilm.setCountry(country);
                    selectedFilm.setLength(length);
                    selectedFilm.setDirector(director);
                    selectedFilm.setActor(actor);
                    selectedFilm.setAgeLimit(ageLimit);
                    selectedFilm.setFilmStatus(status);
                    selectedFilm.setTrailer(trailer);
                    selectedFilm.setContent(content);
                    selectedFilm.setReleaseDate(java.sql.Timestamp.valueOf(localDate.atStartOfDay()));
                    selectedFilm.setAdPosterUrl(adPosterPath);
                    selectedFilm.setPosterUrl(posterPath);

                    boolean updated = FilmDao.updateFilm(selectedFilm);
                    if (updated) {
                        CustomAlert.showInfo("", "Hoàn tất", "Bạn vừa cập nhật phim");
                        tableFilm.refresh();
                    } else {
                        CustomAlert.showError("", "Có lỗi xảy ra", "Cập nhật thất bại.");
                    }

                    addFilmBtn.setOnAction(this::onClickAddFilm);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    CustomAlert.showError("Lỗi định dạng", "Có lỗi xảy ra", "Đã xảy ra lỗi: " + ex.getMessage());
                }
            });
        } else {
            CustomAlert.showError("Lỗi định dạng", "Có lỗi xảy ra", "Vui lòng chọn một phim để chỉnh sửa.");
        }
    }



    public void onClickAdPoster(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh Poster quảng cáo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Copy ảnh vào thư mục dự án
                adPosterPath = copyImageToFolder(selectedFile, "poster");

                // Set lại ImageView
                Image image = new Image(selectedFile.toURI().toString());
                adPosterImg.setImage(image);

                // Cập nhật nút
                adPosterBtn.setText("Đã chọn");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickPoster(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh Poster chính");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                posterImg.setImage(image);

                String fileNameWithoutExtension = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.'));
                // Thêm đường dẫn mới (bỏ đuôi file)
                posterPath = copyImageToFolder(selectedFile, "poster/" + fileNameWithoutExtension);
                posterBtn.setText("Đã chọn");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void onSearchFilm() {
        String keyword = searchFilm.getText().trim().toLowerCase();

        // Nếu rỗng -> load lại toàn bộ danh sách
        if (keyword.isEmpty()) {
            tableFilm.setItems(filmList);
            return;
        }

        // Tìm kiếm theo tên (có thể mở rộng tìm theo country, actor,...)
        ObservableList<Film> filteredList = FXCollections.observableArrayList();

        for (Film film : filmList) {
            if (film.getName().toLowerCase().contains(keyword) ||
                    film.getCountry().toLowerCase().contains(keyword) ||
                    film.getActor().toLowerCase().contains(keyword) ||
                    film.getDirector().toLowerCase().contains(keyword)) {
                filteredList.add(film);
            }
        }

        tableFilm.setItems(filteredList);
    }

}
