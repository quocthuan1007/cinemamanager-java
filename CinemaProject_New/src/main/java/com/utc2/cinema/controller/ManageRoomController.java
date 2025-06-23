package com.utc2.cinema.controller;

import com.utc2.cinema.dao.RoomDao;
import com.utc2.cinema.dao.SeatDao;
import com.utc2.cinema.model.entity.CustomAlert;
import com.utc2.cinema.model.entity.Room;
import com.utc2.cinema.model.entity.Seats;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;

public class ManageRoomController {

    @FXML
    private TextField roomNameField;

    @FXML
    private TextField rowCountField;

    @FXML
    private TextField columnCountField;

    @FXML
    private ComboBox<String> roomStatusComboBox;

    @FXML
    private GridPane seatGrid;

    @FXML
    private Button addRoomBtn;

    @FXML
    private Button deleteRoomBtn;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> roomNameComboBox;

    @FXML
    private Pane deleteConfirmationPane;

    private final RoomDao roomDao = new RoomDao();
    private final SeatDao seatDao = new SeatDao();
    private Room currentRoom;

    public ManageRoomController(MainManagerController mainMenu) {

        this.roomNameField = mainMenu.getRoomNameField();
        this.rowCountField = mainMenu.getRowCountField();
        this.columnCountField = mainMenu.getColumnCountField();
        this.roomStatusComboBox = mainMenu.getRoomStatusComboBox();
        this.seatGrid = mainMenu.getSeatGrid();

        this.addRoomBtn = mainMenu.getAddRoomBtn();
        this.deleteRoomBtn = mainMenu.getDeleteRoomBtn();
        this.saveButton = mainMenu.getSaveButton();
        this.cancelButton = mainMenu.getCancelButton();
        this.roomNameComboBox = mainMenu.getRoomNameComboBox();
        this.deleteConfirmationPane = mainMenu.getDeleteConfirmationPane();
    }


    @FXML
    public void initialize() {
        roomStatusComboBox.setItems(FXCollections.observableArrayList("Bình thường", "Bảo trì"));
        roomStatusComboBox.getSelectionModel().selectFirst();

        updateRoomNameComboBox();

        roomNameComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Room selectedRoom = roomDao.getRoomByName(newVal);
                if (selectedRoom != null) {
                    currentRoom = selectedRoom;
                    roomNameField.setText(selectedRoom.getName());
                    rowCountField.setText(String.valueOf(selectedRoom.getNumRows()));
                    columnCountField.setText(String.valueOf(selectedRoom.getNumCols()));
                    roomStatusComboBox.setValue(selectedRoom.getRoomStatus());
                    openSeatSelection(selectedRoom);
                }
            }
        });

        saveButton.setOnAction(event -> saveRoom());
        cancelButton.setOnAction(event -> cancelEdit());
        deleteRoomBtn.setOnAction(event -> showDeleteConfirmation());
    }

    private void updateRoomNameComboBox() {
        List<String> roomNames = roomDao.getAllRoomNames();
        roomNameComboBox.setItems(FXCollections.observableArrayList(roomNames));

        if (!roomNames.isEmpty()) {
            roomNameComboBox.getSelectionModel().selectFirst();
            String selectedName = roomNameComboBox.getSelectionModel().getSelectedItem();
            currentRoom = roomDao.getRoomByName(selectedName);
            if (currentRoom != null) {
                openSeatSelection(currentRoom);
            }
        } else {
            currentRoom = null;
            clearFields();
            seatGrid.getChildren().clear(); // Xóa sơ đồ ghế nếu không còn phòng
        }
    }


    public void openSeatSelection(Room room) {
        int rows = room.getNumRows();
        int columns = room.getNumCols();

        currentRoom = room;
        roomNameField.setText(room.getName());
        rowCountField.setText(String.valueOf(rows));
        columnCountField.setText(String.valueOf(columns));
        roomStatusComboBox.setValue(room.getRoomStatus());

        seatGrid.getChildren().clear();
        seatGrid.setPadding(new Insets(30));
        seatGrid.setHgap(10);
        seatGrid.setVgap(10);
        seatGrid.setAlignment(Pos.CENTER);

        Image seatNormal = new Image(getClass().getResourceAsStream("/Image/ghe thuong.png"));
        Image seatVip = new Image(getClass().getResourceAsStream("/Image/ghe vip.png"));

        int centerColumn = columns / 2;

        // Tạo các nhãn cho các cột
        for (int j = 0; j < columns; j++) {
            Label columnLabel = new Label(String.valueOf(j + 1));
            columnLabel.setPrefSize(60, 60);
            columnLabel.setAlignment(Pos.CENTER);
            columnLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            seatGrid.add(columnLabel, j + 1, 0);
        }
        roomDao.clearSeatsForRoom(room.getId());
        // Cập nhật ghế cho phòng hiện tại (xóa ghế cũ và tạo lại ghế mới)
        updateSeatsForRoom(room, rows, columns, centerColumn);

        // Tạo các nhãn cho các hàng và ghế
        for (int i = 0; i < rows; i++) {
            char rowLetter = (char) ('A' + i);
            Label rowLabel = new Label(String.valueOf(rowLetter));
            rowLabel.setPrefSize(60, 60);
            rowLabel.setAlignment(Pos.CENTER);
            rowLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            seatGrid.add(rowLabel, 0, i + 1);

            for (int j = 0; j < columns; j++) {
                String seatName = rowLetter + String.valueOf(j + 1);
                Button btn = new Button();
                btn.setPrefSize(60, 60);
                btn.setCursor(Cursor.HAND);

                boolean isVip = (columns % 2 == 0 && (j == centerColumn - 1 || j == centerColumn)) ||
                        (columns % 2 != 0 && j == centerColumn);

                ImageView iv = new ImageView(isVip ? seatVip : seatNormal);
                iv.setFitWidth(50);
                iv.setFitHeight(50);
                iv.setPreserveRatio(true);
                btn.setGraphic(iv);

                btn.setStyle(isVip
                        ? "-fx-background-color: gold; -fx-border-color: black; -fx-border-radius: 5px;"
                        : "-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 5px;");

                Tooltip tip = new Tooltip("Ghế " + seatName + (isVip ? " (VIP)" : ""));
                Tooltip.install(btn, tip);

                seatGrid.add(btn, j + 1, i + 1);
            }
        }
    }

    private void updateSeatsForRoom(Room room, int rows, int columns, int centerColumn) {
        // Lấy danh sách ghế cũ trong phòng này
        List<Seats> existingSeats = seatDao.getSeatsByRoom(room.getId());

        List<Seats> newSeats = new ArrayList<>();
        Set<String> existingPositions = new HashSet<>();

        // Lưu các vị trí ghế đã tồn tại
        for (Seats seat : existingSeats) {
            existingPositions.add(seat.getPosition());
        }

        // Tạo ghế mới và kiểm tra sự thay đổi
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String position = (char) ('A' + i) + String.valueOf(j + 1);
                boolean isVip = (columns % 2 == 0 && (j == centerColumn - 1 || j == centerColumn)) ||
                        (columns % 2 != 0 && j == centerColumn);

                int seatTypeId = isVip ? 2 : 1; // Giả sử 1 là ghế thường, 2 là ghế VIP

                // Nếu ghế chưa tồn tại, tạo mới
                if (!existingPositions.contains(position)) {
                    Seats seat = new Seats(position, room.getId(), seatTypeId);
                    newSeats.add(seat);
                }
            }
        }

        // Chèn tất cả ghế mới vào cơ sở dữ liệu trong một lần duy nhất
        if (!newSeats.isEmpty()) {
            seatDao.addSeatsBatch(newSeats); // Cập nhật hàm thêm ghế theo lô
        }
    }


    private void saveRoom() {
        String name = roomNameField.getText();
        String rowText = rowCountField.getText();
        String colText = columnCountField.getText();
        String status = roomStatusComboBox.getValue();

        if (name.isEmpty() || rowText.isEmpty() || colText.isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        int rows;
        int columns;
        try {
            rows = Integer.parseInt(rowText);
            columns = Integer.parseInt(colText);
        } catch (NumberFormatException e) {
            showAlert("Số hàng và số cột phải là số nguyên.");
            return;
        }

        if (rows <= 0 || columns <= 0) {
            showAlert("Số hàng và số cột phải lớn hơn 0.");
            return;
        }

        if (currentRoom == null) {
            if (roomDao.getRoomByName(name) != null) {
                showAlert("Tên phòng đã tồn tại. Vui lòng chọn tên khác.");
                return;
            }

            Room newRoom = new Room(name, rows, columns, status);
            roomDao.addRoom(newRoom);
        } else {
            currentRoom.setName(name);
            currentRoom.setNumRows(rows);
            currentRoom.setNumCols(columns);
            currentRoom.setRoomStatus(status);
            roomDao.updateRoom(currentRoom);
        }

        updateRoomNameComboBox();
        clearFields();
        currentRoom = null;
    }

    private void showDeleteConfirmation() {
        deleteConfirmationPane.setVisible(true);
    }

    void onConfirmDelete() {
        if (currentRoom != null) {
            boolean hasReservation = roomDao.hasAnyReservations(currentRoom.getId());
            if (hasReservation) {
                CustomAlert.showError("Không thể xóa", "Phòng này đang có vé được đặt. Vui lòng hủy các vé trước khi xóa phòng.");
                deleteConfirmationPane.setVisible(false);
                return;
            }

            // Xóa nếu không có liên kết
            roomDao.deleteRoom(currentRoom.getName());
            currentRoom = null;
            updateRoomNameComboBox();
            clearFields();
            seatGrid.getChildren().clear();
        }
        deleteConfirmationPane.setVisible(false);
    }


    @FXML
    void onCancelDelete() {
        deleteConfirmationPane.setVisible(false);
    }

    private void clearFields() {
        roomNameField.clear();
        rowCountField.clear();
        columnCountField.clear();
        roomStatusComboBox.getSelectionModel().selectFirst();
    }

    private void addRoom() {
        currentRoom = null;
        clearFields();
        roomNameField.setDisable(false);
        rowCountField.setDisable(false);
        columnCountField.setDisable(false);
        roomStatusComboBox.setDisable(false);
    }

    private void cancelEdit() {
        clearFields();
        currentRoom = null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
