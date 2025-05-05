package com.utc2.cinema.controller;

import com.utc2.cinema.dao.FilmDao;
import com.utc2.cinema.dao.MovieShowDao;
import com.utc2.cinema.dao.RoomDao;
import com.utc2.cinema.model.entity.Film;
import com.utc2.cinema.model.entity.MovieShow;
import com.utc2.cinema.model.entity.Room;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageScheduleController  {

    @FXML private TableView<MovieShow> scheduleTable;
    @FXML private TableColumn<MovieShow, String> colStart;
    @FXML private TableColumn<MovieShow, String> colEnd;
    @FXML private TableColumn<MovieShow, String> colFilm;
    @FXML private TableColumn<MovieShow, Integer> colRoom;
    @FXML private TableColumn<MovieShow, Void> colDelete;


    @FXML private Pane confirmDeletePane;
    @FXML private Pane removePane;
    @FXML private Pane RemoveSuccessPane;
    @FXML private Pane addMovieShowPane;

    @FXML private ComboBox<Film> filmComboBox;
    @FXML private ComboBox<Integer> roomComboBox;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML
    private TextField searchField;

    public ManageScheduleController(MainManagerController mainMenu) {
        this.scheduleTable = mainMenu.getScheduleTable();
        this.colStart = mainMenu.getColStart();
        this.colEnd = mainMenu.getColEnd();
        this.colFilm = mainMenu.getColFilm();
        this.colRoom = mainMenu.getColRoom();
        this.colDelete = mainMenu.getColDelete();

        this.confirmDeletePane = mainMenu.getConfirmDeletePane();
        this.removePane = mainMenu.getRemovePane();
        this.RemoveSuccessPane = mainMenu.getRemoveSuccessPane();
        this.addMovieShowPane = mainMenu.getAddMovieShowPane();

        this.filmComboBox = mainMenu.getFilmComboBox();
        this.roomComboBox = mainMenu.getRoomComboBox();
        this.startTimeField = mainMenu.getStartTimeField();
        this.endTimeField = mainMenu.getEndTimeField();

        this.searchField = mainMenu.getSearchField();
    }


    private final MovieShowDao movieShowDao = new MovieShowDao();
    private final FilmDao filmDao = new FilmDao();
    private final RoomDao roomDao = new RoomDao();
    private final ObservableList<MovieShow> movieShowList = FXCollections.observableArrayList();

    private MovieShow selectedMovieShow; // Lưu tạm lịch chiếu cần xóa

    @FXML
    public void initialize() {
        loadMovieShows();
        setupTableColumns();
        styleTableRows();
        loadFilmComboBox();
        loadRoomComboBox();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            onSearchMovieShow();
        });
    }

    private void loadMovieShows() {
        List<MovieShow> shows = movieShowDao.getAllMovieShows();
        movieShowList.setAll(shows);
        scheduleTable.setItems(movieShowList);
    }

    private void styleTableRows() {
        scheduleTable.setRowFactory(tv -> {
            TableRow<MovieShow> row = new TableRow<>();
            row.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            return row;
        });
    }


private void setupTableColumns() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM");

    // Tạo một map chứa thông tin phim (nếu có nhiều phim, sẽ giúp giảm thiểu số lần gọi đến DB)
    Map<Integer, Film> filmMap = new HashMap<>();
    List<Film> films = filmDao.getAllFilms();
    for (Film film : films) {
        filmMap.put(film.getId(), film);
    }

    // Cột thời gian bắt đầu
    colStart.setCellValueFactory(cell ->
            Bindings.createStringBinding(() -> cell.getValue().getStartTime().format(formatter))
    );

    // Cột thời gian kết thúc
    colEnd.setCellValueFactory(cell ->
            Bindings.createStringBinding(() -> cell.getValue().getEndTime().format(formatter))
    );

    // Cột phim
    colFilm.setCellValueFactory(cell -> {
        Film film = filmMap.get(cell.getValue().getFilmId());
        return Bindings.createStringBinding(() -> film != null ? film.getName() : "Không rõ");
    });

    // Cột phòng
    colRoom.setCellValueFactory(new PropertyValueFactory<>("roomId"));

    // Thêm nút xóa vào bảng nếu cần thiết
    addDeleteButtonToTable();
}


    private void addDeleteButtonToTable() {
        colDelete.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Xóa");

            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                deleteBtn.setOnAction(event -> {
                    selectedMovieShow = getTableView().getItems().get(getIndex());
                    confirmDeletePane.setVisible(true);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });
    }

    @FXML
    void onCancelDelete() {
        confirmDeletePane.setVisible(false);
    }

    @FXML
    void onConfirmDelete() {
        if (selectedMovieShow != null) {
            movieShowDao.deleteMovieShowById(selectedMovieShow.getId()); // Xóa trong DB
            movieShowList.remove(selectedMovieShow); // Xóa mục trong bảng
            selectedMovieShow = null;
            confirmDeletePane.setVisible(false);
            RemoveSuccessPane.setVisible(true); // Hiển thị thông báo thành công
        }
    }

    @FXML
    void onBackMannagerSchedule() {
        if (removePane != null) {
            removePane.setVisible(false);
        }
        confirmDeletePane.setVisible(false);
        RemoveSuccessPane.setVisible(false);
    }

    private void loadFilmComboBox() {
        List<Film> films = filmDao.getAllFilms(); // Danh sách Film
        filmComboBox.setItems(FXCollections.observableArrayList(films)); // Đưa vào ComboBox

        // Tùy chỉnh hiển thị chỉ tên phim
        filmComboBox.setCellFactory(lv -> new ListCell<Film>() {
            @Override
            protected void updateItem(Film film, boolean empty) {
                super.updateItem(film, empty);
                setText(empty || film == null ? null : film.getName());
            }
        });
        filmComboBox.setButtonCell(new ListCell<Film>() {
            @Override
            protected void updateItem(Film film, boolean empty) {
                super.updateItem(film, empty);
                setText(empty || film == null ? null : film.getName());
            }
        });
    }


    private void loadRoomComboBox() {
        // Lấy danh sách các phòng từ RoomDao
        List<Room> rooms = roomDao.getAllRooms(); // Danh sách phòng

        // Chuyển đổi danh sách phòng thành danh sách ID phòng
        List<Integer> roomIds = new ArrayList<>();
        for (Room room : rooms) {
            roomIds.add(room.getId()); // Lấy ID của từng phòng và thêm vào danh sách
        }

        // Đưa danh sách ID phòng vào ComboBox
        roomComboBox.setItems(FXCollections.observableArrayList(roomIds));
    }


    @FXML
    void onAddShowClick() {
        addMovieShowPane.setVisible(true);
    }

    @FXML
    void onSaveMovieShow() {
        try {
            String startTimeText = startTimeField.getText();
            String endTimeText = endTimeField.getText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            LocalDateTime startTime = LocalDateTime.parse(startTimeText, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeText, formatter);

            // Kiểm tra thời gian bắt đầu có phải trong quá khứ không
            if (startTime.isBefore(LocalDateTime.now())) {
                showAlert("Lỗi thời gian", "Thời gian bắt đầu không thể là quá khứ.");
                return;
            }

            if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
                showAlert("Lỗi thời gian", "Thời gian bắt đầu phải trước thời gian kết thúc.");
                return;
            }

            Film selectedFilm = filmComboBox.getSelectionModel().getSelectedItem();
            Integer selectedRoom = roomComboBox.getSelectionModel().getSelectedItem();

            if (selectedFilm == null || selectedRoom == null) {
                showAlert("Thiếu thông tin", "Vui lòng chọn phim và phòng chiếu.");
                return;
            }

            // Kiểm tra trùng lịch
            List<MovieShow> shows = movieShowDao.getAllMovieShows();
            for (MovieShow existingShow : shows) {
                if (existingShow.getRoomId() == selectedRoom &&
                        startTime.isBefore(existingShow.getEndTime()) &&
                        endTime.isAfter(existingShow.getStartTime())) {
                    showAlert("Trùng lịch", "Lịch chiếu trùng với một lịch đã có trong cùng phòng.");
                    return;
                }
            }

            MovieShow movieShow = new MovieShow(startTime, endTime, selectedFilm.getId(), selectedRoom, false);
            movieShowDao.saveMovieShow(movieShow);
            movieShowList.add(movieShow);
            addMovieShowPane.setVisible(false);

        } catch (Exception e) {
            showAlert("Lỗi định dạng", "Vui lòng nhập đúng định dạng thời gian: HH:mm dd/MM/yyyy");
        }
    }



    @FXML
    void onCancelAddMovieShow() {
        addMovieShowPane.setVisible(false); // Hủy thêm lịch chiếu
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void onSearchMovieShow() {
        String keyword = searchField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            // Nếu không nhập gì thì hiển thị toàn bộ danh sách
            scheduleTable.setItems(movieShowList);
            return;
        }

        // Tạo Map filmId -> Film để tra cứu tên phim
        Map<Integer, Film> filmMap = new HashMap<>();
        for (Film film : filmDao.getAllFilms()) {
            filmMap.put(film.getId(), film);
        }

        // Lọc danh sách
        ObservableList<MovieShow> filteredList = FXCollections.observableArrayList();
        for (MovieShow show : movieShowList) {
            Film film = filmMap.get(show.getFilmId());
            if (film != null && film.getName().toLowerCase().contains(keyword)) {
                filteredList.add(show);
            }
        }

        scheduleTable.setItems(filteredList);
    }



}
