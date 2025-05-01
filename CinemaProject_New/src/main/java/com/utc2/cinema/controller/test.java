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
import java.util.List;

public class test {

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

        colStart.setCellValueFactory(cell ->
                Bindings.createStringBinding(() -> cell.getValue().getStartTime().format(formatter))
        );

        colEnd.setCellValueFactory(cell ->
                Bindings.createStringBinding(() -> cell.getValue().getEndTime().format(formatter))
        );

        colFilm.setCellValueFactory(cell -> {
            Film film = filmDao.getFilmById(cell.getValue().getFilmId());
            return Bindings.createStringBinding(() -> film != null ? film.getName() : "Không rõ");
        });

        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomId"));

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
    private void onCancelDelete() {
        confirmDeletePane.setVisible(false);
    }

    @FXML
    private void onConfirmDelete() {
        if (selectedMovieShow != null) {
            movieShowDao.deleteMovieShowById(selectedMovieShow.getId()); // Xóa trong DB
            movieShowList.remove(selectedMovieShow); // Xóa mục trong bảng
            selectedMovieShow = null;
            confirmDeletePane.setVisible(false);
            RemoveSuccessPane.setVisible(true); // Hiển thị thông báo thành công
        }
    }

    @FXML
    private void onBackMannagerSchedule() {
        if (removePane != null) {
            removePane.setVisible(false);
        }
        confirmDeletePane.setVisible(false);
        RemoveSuccessPane.setVisible(false);
    }

    private void loadFilmComboBox() {
        List<Film> films = filmDao.getAllFilms();
        filmComboBox.setItems(FXCollections.observableArrayList(films));
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
    private void onAddShowClick() {
        addMovieShowPane.setVisible(true);
    }

    @FXML
    private void onSaveMovieShow() {
        String startTimeText = startTimeField.getText();
        String endTimeText = endTimeField.getText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startTime = LocalDateTime.parse(startTimeText, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeText, formatter);

        Film selectedFilm = filmComboBox.getSelectionModel().getSelectedItem();
        Integer selectedRoom = roomComboBox.getSelectionModel().getSelectedItem();

        if (selectedFilm != null && selectedRoom != null) {
            MovieShow movieShow = new MovieShow(startTime, endTime, selectedFilm.getId(), selectedRoom, false);
            movieShowDao.saveMovieShow(movieShow);

            movieShowList.add(movieShow);  // Thêm lịch chiếu mới vào bảng
            addMovieShowPane.setVisible(false); // Ẩn form thêm lịch chiếu
        }
    }

    @FXML
    private void onCancelAddMovieShow() {
        addMovieShowPane.setVisible(false); // Hủy thêm lịch chiếu
    }
}
