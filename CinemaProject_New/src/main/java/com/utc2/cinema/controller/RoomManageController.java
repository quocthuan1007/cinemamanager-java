package com.utc2.cinema.controller;

import com.utc2.cinema.dao.RoomDao;
import com.utc2.cinema.model.entity.Room;
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
import java.util.List;
import java.util.ResourceBundle;

public class RoomManageController implements Initializable {

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

    private final RoomDao roomDao = new RoomDao(); // DAO để thao tác với DB
    private Room currentRoom; // Để lưu trữ phòng hiện tại khi chỉnh sửa

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập các giá trị mặc định cho ComboBox
        roomStatusComboBox.setItems(FXCollections.observableArrayList("Bình thường", "Bảo trì"));
        roomStatusComboBox.getSelectionModel().selectFirst();



        roomNameComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Room selectedRoom = roomDao.getRoomByName(newVal); // Gọi DAO để lấy Room theo tên
                if (selectedRoom != null) {
                    currentRoom = selectedRoom; // Lưu phòng đang được chọn
                    roomNameField.setText(selectedRoom.getName());
                    rowCountField.setText(String.valueOf(selectedRoom.getNumRows()));
                    columnCountField.setText(String.valueOf(selectedRoom.getNumCols()));
                    roomStatusComboBox.setValue(selectedRoom.getRoomStatus());

                    // Nếu muốn hiển thị luôn sơ đồ ghế
                    openSeatSelection(selectedRoom);
                }
            }
        });

        // Thêm sự kiện cho nút Lưu
        saveButton.setOnAction(event -> saveRoom());

        // Thêm sự kiện cho nút Hủy
        cancelButton.setOnAction(event -> cancelEdit());

        // Thêm sự kiện cho nút Xóa phòng chiếu
        deleteRoomBtn.setOnAction(event -> showDeleteConfirmation());
    }

    private void updateRoomNameComboBox() {
        // Giả sử bạn có phương thức lấy tất cả các tên phòng từ RoomDao
        List<String> roomNames = roomDao.getAllRoomNames(); // Phương thức này trả về danh sách tên phòng chiếu

        // Cập nhật ComboBox với danh sách tên phòng chiếu
        roomNameComboBox.setItems(FXCollections.observableArrayList(roomNames));

        // Nếu danh sách có ít nhất một phòng, chọn phòng đầu tiên mặc định
        if (!roomNames.isEmpty()) {
            roomNameComboBox.getSelectionModel().selectFirst();
        }
    }

    // Hàm hiển thị sơ đồ ghế
    public void openSeatSelection(Room room) {
        int rows = room.getNumRows();
        int columns = room.getNumCols();

        currentRoom = room; // Lưu trữ phòng hiện tại khi mở sơ đồ ghế
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

        for (int j = 0; j < columns; j++) {
            Label columnLabel = new Label(String.valueOf(j + 1));
            columnLabel.setPrefSize(60, 60);
            columnLabel.setAlignment(Pos.CENTER);
            columnLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            seatGrid.add(columnLabel, j + 1, 0);
        }

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

    // Phương thức lưu phòng chiếu
    // Phương thức lưu phòng chiếu
    private void saveRoom() {
        String name = roomNameField.getText();
        int rows = Integer.parseInt(rowCountField.getText());
        int columns = Integer.parseInt(columnCountField.getText());
        String status = roomStatusComboBox.getValue();

        if (currentRoom == null) {
            Room newRoom = new Room(name, rows, columns, status);
            roomDao.addRoom(newRoom);
        } else {
            boolean isSeatLayoutChanged = (rows != currentRoom.getNumRows() || columns != currentRoom.getNumCols());
            // Kiểm tra xem có ghế đã được đặt chưa
            // Kiểm tra nếu thay đổi sơ đồ ghế và có vé đặt
            if (isSeatLayoutChanged && roomDao.hasFutureReservations(currentRoom.getId())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Không thể cập nhật sơ đồ ghế");
                alert.setContentText("Phòng này đã có ghế được đặt trong tương lai. Không thể thay đổi số hàng hoặc số cột.");
                alert.showAndWait();
                return;
            }

            currentRoom.setName(name);
            currentRoom.setNumRows(rows);
            currentRoom.setNumCols(columns);
            currentRoom.setRoomStatus(status);
            roomDao.updateRoom(currentRoom);
        }

        updateRoomNameComboBox();
        clearFields();
    }



    // Phương thức xóa phòng chiếu
    private void showDeleteConfirmation() {
        deleteConfirmationPane.setVisible(true);
    }

    @FXML
    private void onConfirmDelete() {
        if (currentRoom != null) {
            roomDao.deleteRoom(currentRoom.getName()); // Gọi DAO để xóa phòng chiếu
            updateRoomNameComboBox(); // Cập nhật lại danh sách phòng chiếu
            clearFields(); // Xóa các trường nhập liệu
        }
        deleteConfirmationPane.setVisible(false); // Ẩn khung xác nhận
    }

    @FXML
    private void onCancelDelete() {
        deleteConfirmationPane.setVisible(false); // Ẩn khung xác nhận
    }

    private void clearFields() {
        roomNameField.clear();
        rowCountField.clear();
        columnCountField.clear();
        roomStatusComboBox.getSelectionModel().selectFirst();
    }

    // Phương thức thêm phòng chiếu
    private void addRoom() {
        currentRoom = null; // Đặt phòng hiện tại là null để thêm phòng mới
        clearFields();
        roomNameField.setDisable(false); // Bật trường tên phòng để người dùng nhập
        rowCountField.setDisable(false); // Bật trường số lượng hàng
        columnCountField.setDisable(false); // Bật trường số lượng cột
        roomStatusComboBox.setDisable(false); // Bật combo box trạng thái phòng
    }


    private void cancelEdit() {
        clearFields(); // Xóa các trường nhập liệu khi hủy
    }
}
