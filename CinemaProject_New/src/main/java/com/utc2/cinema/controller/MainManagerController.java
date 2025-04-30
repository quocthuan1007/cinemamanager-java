package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.model.entity.Account;
import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.model.entity.User;
import com.utc2.cinema.model.entity.UserAccount;
import com.utc2.cinema.service.AccountService;
import com.utc2.cinema.service.UserService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainManagerController implements Initializable {
    public TableColumn<UserAccount, String> getEmailUser() {
        return emailUser;
    }

    public TableColumn<UserAccount, String> getGenderUser() {
        return genderUser;
    }

    public TableColumn<UserAccount, String> getNameUser() {
        return nameUser;
    }

    public TableColumn<UserAccount, String> getNumUser() {
        return numUser;
    }

    public TableColumn<UserAccount, String> getAddUser() {
        return addUser;
    }

    public TableColumn<UserAccount, Date> getBirthUser() {
        return birthUser;
    }

    public TableView<UserAccount> getTableUser() {
        return tableUser;
    }

    public ObservableList<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    public TextField getAddressConfirm() {
        return addressConfirm;
    }

    public DatePicker getBirthConfirm() {
        return birthConfirm;
    }

    public Button getCloseConfirm() {
        return closeConfirm;
    }

    public ChoiceBox<String> getGenderConfirm() {
        return genderConfirm;
    }

    public Pane getInfoForm() {
        return infoForm;
    }

    public TextField getNameConfirm() {
        return nameConfirm;
    }

    public TextField getNumberConfirm() {
        return numberConfirm;
    }

    public Button getSaveConfirm() {
        return saveConfirm;
    }
    //===
    //confirm
    @FXML
    private TextField addressConfirm;

    @FXML
    private DatePicker birthConfirm;
    @FXML
    private Button closeConfirm;
    @FXML
    private ChoiceBox<String> genderConfirm;
    @FXML
    private Pane infoForm;
    @FXML
    private TextField nameConfirm;
    @FXML
    private TextField numberConfirm;
    @FXML
    private Button saveConfirm;
    //
    @FXML
    private TableColumn<UserAccount, String> addUser;

    @FXML
    private TableColumn<UserAccount, Date> birthUser;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    @FXML
    private TableColumn<UserAccount, String> emailUser;

    @FXML
    private TextField searchEmailField;

    @FXML
    private TableColumn<UserAccount, String> genderUser;

    @FXML
    private TableColumn<UserAccount, String> nameUser;

    @FXML
    private TableColumn<UserAccount, String> numUser;

    @FXML
    private VBox sidebar;

    @FXML
    private TableView<UserAccount> tableUser;

    @FXML
    private Button viewBtn;

    private ManageUserController userController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userController = new ManageUserController(this);
        userController.init();
    }
    @FXML
    void handleDangXuat(ActionEvent event) {
        System.out.println("Đăng xuất");
    }

    @FXML
    void handleLichChieu(ActionEvent event) {
        System.out.println("Lịch chiếu");
    }

    @FXML
    void handleNhanVien(ActionEvent event) {
        System.out.println("Nhân viên");
    }

    @FXML
    void handlePhongChieu(ActionEvent event) {
        System.out.println("Phòng chiếu");
    }

    @FXML
    void handleQuanLyPhim(ActionEvent event) {
        System.out.println("Quản lý phim");
    }

    @FXML
    void handleThongKe(ActionEvent event) {
        System.out.println("Thống kê");
    }
    private ObservableList<UserAccount> userAccounts = FXCollections.observableArrayList();


    @FXML
    void onClickDelete(ActionEvent event)
    {
        userController.onClickDelete(event);
    }

    @FXML
    void onClickEdit(ActionEvent event) {
        userController.onClickEdit(event);
    }

    @FXML
    void onClickView(ActionEvent event) {
        userController.onClickView(event);
    }
    //confirm

    @FXML
    void onPlayerCloseInfoConfirm(ActionEvent event) {
        userController.onPlayerCloseInfoConfirm(event);
    }

    public TextField getSearchEmailField() {
        return searchEmailField;
    }

    @FXML
    void onPlayerSaveInfoConfirm(ActionEvent event) {
        userController.onPlayerSaveInfoConfirm(event);
    }
    @FXML
    void onSearchByEmail(ActionEvent event)
    {
        userController.onSearchByEmail(event);
    }
    //===========================================================film-manage=====================================
    //===========================================================================================================
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
