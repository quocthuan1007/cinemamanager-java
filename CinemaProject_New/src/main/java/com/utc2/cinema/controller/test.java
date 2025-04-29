package com.utc2.cinema.controller;

import com.utc2.cinema.dao.*;
import com.utc2.cinema.model.entity.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class test {

    @FXML
    private VBox scheduleContainerOfFilm;
    @FXML
    private VBox filmsContainer;
    @FXML
    private VBox filmListVBox; // Thêm phần chọn phim
    @FXML
    private VBox scheduleListVBox; // Thêm phần chọn suất chiếu
    @FXML
    private AnchorPane seatSelectionPane; // Phần chọn ghế
    @FXML
    private Button backButton; // Nút quay lại
    @FXML
    private GridPane seatGrid;
    @FXML
    private Label screenLabel;
    @FXML
    private VBox foodDrinkVBox;

    private final MovieShowDao movieShowDao = new MovieShowDao();
    private final RoomDao roomDao = new RoomDao();
    private final FilmDao filmDao = new FilmDao();

    @FXML
    public void initialize() {
        showAllFilms();

        // Thiết lập sự kiện cho nút quay lại
        backButton.setOnAction(event -> {
            // Hiển thị lại phần chọn phim và suất chiếu
            filmListVBox.setVisible(true);
            scheduleListVBox.setVisible(true);

            // Ẩn phần chọn ghế
            seatSelectionPane.setVisible(false);

            screenLabel.setAlignment(Pos.CENTER);
        });
        loadFoodList();  // Đảm bảo rằng foodList đã được tải từ cơ sở dữ liệu
        // Khởi tạo TableView
        initializeTable();
        // Hiển thị các món ăn đã tải lên bảng
        displayFoodOrders();
    }

    private void showAllFilms() {
        List<Film> allFilms = FilmDao.getAllFilms();
        filmsContainer.getChildren().clear();

        for (Film film : allFilms) {
            String posterPath = "/Image/" + film.getPosterUrl() + ".png";
            InputStream is = getClass().getResourceAsStream(posterPath);

            if (is == null) {
                System.out.println("Không tìm thấy ảnh: " + posterPath);
                continue;
            }

            Image image = new Image(is);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(60);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            Label filmNameLabel = new Label(film.getName());
            filmNameLabel.setFont(new Font("Arial", 16));
            filmNameLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

            String ageLimit = getAgeLimitSymbol(film.getAgeLimit());

            Label ageLimitLabel = new Label(ageLimit);
            if ("PG".equals(ageLimit)) {
                ageLimitLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: green; -fx-padding: 5px; -fx-background-radius: 5px;");
            } else {
                ageLimitLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: red; -fx-padding: 5px; -fx-background-radius: 5px;");
            }
            ageLimitLabel.setPrefWidth(50);
            ageLimitLabel.setMaxWidth(50);

            HBox leftBox = new HBox(10);
            leftBox.setStyle("-fx-alignment: center-left;");
            leftBox.getChildren().addAll(imageView, filmNameLabel);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox rightBox = new HBox();
            rightBox.setStyle("-fx-alignment: center; -fx-padding: 5px;");
            rightBox.getChildren().add(ageLimitLabel);

            HBox filmBox = new HBox();
            filmBox.setStyle("-fx-alignment: center; -fx-padding: 10px; -fx-border-color: black; -fx-border-radius: 10px; -fx-background-radius: 10px;");
            filmBox.getChildren().addAll(leftBox, spacer, rightBox);

            filmBox.setOnMouseClicked(event -> {
                showMovieShowOfFilm(film.getId());
            });

            filmsContainer.getChildren().add(filmBox);
        }
    }

    private void showMovieShowOfFilm(int filmId) {
        scheduleContainerOfFilm.getChildren().clear();
        List<MovieShow> movieShows = movieShowDao.getMovieShowsFromTodayOnward(filmId);
        LocalDate today = LocalDate.now();

        Map<LocalDate, List<MovieShow>> showsByDate = new HashMap<>();

        for (MovieShow show : movieShows) {
            if (show.getStartTime().toLocalDate().isBefore(today)) {
                continue;
            }
            showsByDate.computeIfAbsent(show.getStartTime().toLocalDate(), k -> new ArrayList<>()).add(show);
        }

        boolean hasValidShow = false;

        for (Map.Entry<LocalDate, List<MovieShow>> entry : showsByDate.entrySet()) {
            LocalDate showDate = entry.getKey();
            List<MovieShow> showsOnDate = entry.getValue();

            String showDateString = showDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Label dayLabel = new Label("Ngày: " + showDateString);
            dayLabel.setFont(new Font("Arial", 16));
            dayLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
            scheduleContainerOfFilm.getChildren().add(dayLabel);

            HBox showtimesHBox = new HBox(20);
            showtimesHBox.setSpacing(10);
            showtimesHBox.setStyle("-fx-wrap-text: true;");

            for (MovieShow show : showsOnDate) {
                String showStart = show.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                String showEnd = show.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                String showInfo = "Bắt đầu: " + showStart + " | Kết thúc: " + showEnd;

                Label showLabel = new Label(showInfo);
                showLabel.setFont(new Font("Arial", 14));
                showLabel.setStyle("-fx-background-color: lightgray; -fx-padding: 10px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: green; -fx-border-width: 2px;");
                showLabel.setMaxWidth(250);
                showLabel.setMaxHeight(100);
                showLabel.setWrapText(true);

                // Fetch reserved seats for the show
                List<String> reservedSeats = ReservationDao.getReservedSeatsByShowId(show.getId());

                showLabel.setOnMouseEntered(event -> {
                    showLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 10px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: green; -fx-border-width: 2px;");
                });

                showLabel.setOnMouseExited(event -> {
                    showLabel.setStyle("-fx-background-color: lightgray; -fx-padding: 10px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: green; -fx-border-width: 2px;");
                });

                showLabel.setOnMouseClicked(event -> {
                    System.out.println("Click suất chiếu: " + showStart + " - " + showEnd);
                    filmListVBox.setVisible(false);
                    scheduleListVBox.setVisible(false);
                    seatSelectionPane.setVisible(true);
                    openSeatSelection(show.getRoomId(), show.getId());
                });

                showtimesHBox.getChildren().add(showLabel);
            }

            scheduleContainerOfFilm.getChildren().add(showtimesHBox);
            hasValidShow = true;
        }

        if (!hasValidShow) {
            Label noShowLabel = new Label("Hiện không có suất chiếu nào từ hôm nay.");
            noShowLabel.setFont(new Font("Arial", 14));
            noShowLabel.setStyle("-fx-text-fill: gray;");
            scheduleContainerOfFilm.getChildren().add(noShowLabel);
        }
    }


    private String getAgeLimitSymbol(int ageLimit) {
        switch (ageLimit) {
            case 6:
                return "C6";
            case 13:
                return "C13";
            case 16:
                return "C16";
            case 18:
                return "C18";
            default:
                return "PG";
        }
    }


    private void openSeatSelection(int roomId, int showId) {
        Room room = roomDao.getRoomById(roomId);
        int rows = room.getNumRows();
        int columns = room.getNumCols();

        seatGrid.getChildren().clear();
        seatGrid.setPadding(new Insets(30)); // Khoảng cách xung quanh ghế
        seatGrid.setHgap(15); // Khoảng cách giữa các ghế ngang
        seatGrid.setVgap(15); // Khoảng cách giữa các ghế dọc
        seatGrid.setAlignment(Pos.CENTER);

        Image seatNormal = new Image(getClass().getResourceAsStream("/Image/ghe thuong.png"));
        Image seatVip = new Image(getClass().getResourceAsStream("/Image/ghe vip.png"));

        // Lấy danh sách ghế đã đặt từ cơ sở dữ liệu
        List<String> reservedSeats = new ReservationDao().getReservedSeatsByShowId(showId);

        int centerColumn = columns / 2;

        // Thêm số cột trên cùng
        for (int j = 0; j < columns; j++) {
            Label columnLabel = new Label(String.valueOf(j + 1));
            columnLabel.setPrefSize(60, 60);
            columnLabel.setAlignment(Pos.CENTER);
            columnLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            seatGrid.add(columnLabel, j + 1, 0);
        }

        for (int i = 0; i < rows; i++) {
            char rowLetter = (char) ('A' + i);

            // Thêm chữ cái cho hàng bên trái
            Label rowLabel = new Label(String.valueOf(rowLetter));
            rowLabel.setPrefSize(60, 60);
            rowLabel.setAlignment(Pos.CENTER);
            rowLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            seatGrid.add(rowLabel, 0, i + 1);

            for (int j = 0; j < columns; j++) {
                String seatLabel = rowLetter + String.valueOf(j + 1);

                Button seatButton = new Button();
                seatButton.setPrefSize(60, 60);
                seatButton.setCursor(Cursor.HAND);

                ImageView imageView;
                boolean isVipSeat;

                // Check ghế VIP
                if ((columns % 2 == 0 && (j == centerColumn - 1 || j == centerColumn)) ||
                        (columns % 2 != 0 && j == centerColumn)) {
                    imageView = new ImageView(seatVip);
                    seatButton.setStyle("-fx-background-color: gold; -fx-border-color: black; -fx-border-radius: 5px;");
                    isVipSeat = true;
                } else {
                    isVipSeat = false;
                    imageView = new ImageView(seatNormal);
                    seatButton.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 5px;");
                }

                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
                seatButton.setGraphic(imageView);

                Tooltip tooltip = new Tooltip("Ghế " + seatLabel);
                tooltip.setStyle("-fx-font-size: 14px; -fx-background-color: lightyellow; -fx-text-fill: black;");
                Tooltip.install(seatButton, tooltip);

                seatButton.setUserData(false); // ban đầu chưa chọn

                // Kiểm tra ghế đã đặt hay chưa
                if (reservedSeats.contains(seatLabel)) {
                    // Tất cả ghế đã đặt sẽ có màu đỏ
                    seatButton.setStyle(isVipSeat
                            ? "-fx-background-color: darkred; -fx-border-color: black; -fx-border-radius: 5px;" // Ghế VIP đã đặt
                            : "-fx-background-color: red; -fx-border-color: black; -fx-border-radius: 5px;"); // Ghế thường đã đặt
                    seatButton.setDisable(true); // Vô hiệu hóa ghế đã đặt

                    // Thêm thông tin "Đã đặt" vào Tooltip của ghế đã đặt
                    tooltip.setText("Ghế " + seatLabel + " - Đã đặt");
                }

                seatButton.setOnAction(event -> {
                    boolean isSelected = (boolean) seatButton.getUserData();

                    if (!isSelected && !reservedSeats.contains(seatLabel)) {
                        seatButton.setStyle(isVipSeat
                                ? "-fx-background-color: green; -fx-border-color: black; -fx-border-radius: 5px;"
                                : "-fx-background-color: green; -fx-border-color: black; -fx-border-radius: 5px;");
                        System.out.println("Đã chọn ghế " + seatLabel);
                    } else if (!reservedSeats.contains(seatLabel)) {
                        seatButton.setStyle(isVipSeat
                                ? "-fx-background-color: gold; -fx-border-color: black; -fx-border-radius: 5px;"
                                : "-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 5px;");
                        System.out.println("Bỏ chọn ghế " + seatLabel);
                    }

                    seatButton.setUserData(!isSelected); // cập nhật lại trạng thái
                });

                seatGrid.add(seatButton, j + 1, i + 1);
            }
        }
    }




    // foodId -> count



    @FXML
    private TableView<FoodOrder> foodDrinkTableView;

    @FXML
    private TableColumn<FoodOrder, String> nameColumn;

    @FXML
    private TableColumn<FoodOrder, String> descriptionColumn;

    @FXML
    private TableColumn<FoodOrder, Integer> priceColumn;

    @FXML
    private TableColumn<FoodOrder, Integer> quantityColumn;

    @FXML
    private TableColumn<FoodOrder, Integer> totalColumn;

    private List<FoodOrder> foodOrderList = new ArrayList<>();
    private ObservableList<FoodOrder> foodOrderObservableList = FXCollections.observableArrayList();
    private List<Food> foodList = new ArrayList<>();

    private void loadFoodList() {
        // Lấy danh sách món ăn từ cơ sở dữ liệu
        foodList = FoodDao.getAllFoods();  // Giả sử FoodDao lấy danh sách món ăn từ cơ sở dữ liệu
        // Sau khi tải danh sách món ăn, có thể thêm các món ăn vào giỏ hàng để hiển thị trong TableView
        for (Food food : foodList) {
            addFoodToOrder(food, 0);  // Giả sử mặc định số lượng là 1
        }
    }


    // Hàm khởi tạo cột cho TableView
// Cập nhật cột số lượng với nút cộng và trừ
    // Hàm khởi tạo cột cho TableView
    private void initializeTable() {
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFood().getName()));
        descriptionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFood().getDescription()));
        priceColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getFood().getCost()).asObject());

        // Cột Số Lượng với nút cộng và trừ
        quantityColumn.setCellFactory(col -> {
            TableCell<FoodOrder, Integer> cell = new TableCell<FoodOrder, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Button minusButton = new Button("-");
                        Button plusButton = new Button("+");

                        // Nút trừ
                        minusButton.setOnAction(event -> {
                            FoodOrder foodOrder = getTableRow().getItem();
                            if (foodOrder.getCount() > 0) {
                                foodOrder.setCount(foodOrder.getCount() - 1);
                                updateTotalPrice(foodOrder);  // Cập nhật lại tổng tiền
                                displayFoodOrders();  // Cập nhật lại bảng
                            }
                        });

                        // Nút cộng
                        plusButton.setOnAction(event -> {
                            FoodOrder foodOrder = getTableRow().getItem();
                            foodOrder.setCount(foodOrder.getCount() + 1);
                            updateTotalPrice(foodOrder);  // Cập nhật lại tổng tiền
                            displayFoodOrders();  // Cập nhật lại bảng
                        });

                        // Hiển thị số lượng và nút cộng/trừ
                        HBox hbox = new HBox(10, minusButton, new Label(String.valueOf(item)), plusButton);
                        setGraphic(hbox);
                    }
                }
            };
            return cell;
        });

        totalColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalPrice()).asObject());
    }

    // Cập nhật tổng tiền của món ăn
    // Cập nhật tổng tiền của món ăn
    private void updateTotalPrice(FoodOrder foodOrder) {
        // Tính toán lại tổng tiền sau khi thay đổi số lượng
        foodOrder.setTotalPrice(foodOrder.getFood().getCost(), foodOrder.getCount());  // Gọi phương thức setTotalPrice đúng cách
    }


    // Cập nhật bảng khi có thay đổi
    private void displayFoodOrders() {
        foodOrderObservableList.clear();  // Xóa danh sách hiện tại
        foodOrderObservableList.addAll(foodOrderList);  // Thêm lại danh sách mới
        foodDrinkTableView.setItems(foodOrderObservableList);  // Cập nhật bảng
    }


    // Hàm thêm món ăn vào giỏ hàng
    public void addFoodToOrder(Food food, int count) {
        // Kiểm tra nếu món ăn đã có trong giỏ hàng thì chỉ cập nhật số lượng
        for (FoodOrder foodOrder : foodOrderList) {
            if (foodOrder.getFood().getId() == food.getId()) {
                foodOrder.setCount(foodOrder.getCount() + count);  // Cập nhật số lượng
                updateTotalPrice(foodOrder);  // Cập nhật lại tổng tiền
                displayFoodOrders();  // Cập nhật lại bảng sau khi thay đổi
                return;
            }
        }

        // Nếu món ăn chưa có trong giỏ hàng, thêm mới
        FoodOrder foodOrder = new FoodOrder(foodOrderList.size() + 1, food.getId(), 1, count, food);
        foodOrder.setTotalPrice(food.getCost(), count);  // Tính tổng tiền của món ăn
        foodOrderList.add(foodOrder);  // Thêm món ăn vào giỏ hàng
        displayFoodOrders();  // Cập nhật lại bảng sau khi thêm món ăn
    }


}











