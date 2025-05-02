package com.utc2.cinema.controller;

import com.utc2.cinema.dao.*;
import com.utc2.cinema.model.entity.*;
import com.utc2.cinema.service.FilmService;
import com.utc2.cinema.service.UserService;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class BuyTicketController {
    private MainMenuController mainMenu = new MainMenuController();

    private Label filmNameLabel1;
    private Label showTimeLabel;
    private Label roomLabel;
    private Label seatLabel;
    private Label comboLabel;
    private Label totalLabel;
    private Pane billPane;
    private Pane paySuccessPane;
    private VBox scheduleContainerOfFilm;
    private VBox filmsContainer;
    private VBox filmListVBox; // Thêm phần chọn phim
    private VBox scheduleListVBox; // Thêm phần chọn suất chiếu
    private AnchorPane seatSelectionPane; // Phần chọn ghế
    private Button backButton; // Nút quay lại
    private GridPane seatGrid;
    private MovieShow selectedMovieShow;
    private Label screenLabel;
    private VBox foodDrinkVBox;
    private TableView<FoodOrder> foodDrinkTableView;
    private TableColumn<FoodOrder, String> nameColumn;
    private TableColumn<FoodOrder, String> descriptionColumn;
    private TableColumn<FoodOrder, Float> priceColumn;
    private TableColumn<FoodOrder, Integer> quantityColumn;
    private TableColumn<FoodOrder, Float> totalColumn;

    private Set<String> selectedSeats = new HashSet<>();
    private double seatTotalPrice = 0;
    private final MovieShowDao movieShowDao = new MovieShowDao();
    private final RoomDao roomDao = new RoomDao();
    private final FilmDao filmDao = new FilmDao();
    private Film selectedFilm;
    private List<FoodOrder> foodOrderList = new ArrayList<>();
    private ObservableList<FoodOrder> foodOrderObservableList = FXCollections.observableArrayList();
    private List<Food> foodList = new ArrayList<>();

    public BuyTicketController(MainMenuController mainMenu) {

        this.filmNameLabel1 = mainMenu.getFilmNameLabel1(); // Giả sử bạn đã tạo getter trong MainMenuController
        this.showTimeLabel = mainMenu.getShowTimeLabel();
        this.roomLabel = mainMenu.getRoomLabel();
        this.seatLabel = mainMenu.getSeatLabel();
        this.comboLabel = mainMenu.getComboLabel();
        this.totalLabel = mainMenu.getTotalLabel();
        this.billPane = mainMenu.getBillPane();
        this.paySuccessPane = mainMenu.getPaySuccessPane();
        this.scheduleContainerOfFilm = mainMenu.getScheduleContainerOfFilm();
        this.filmsContainer = mainMenu.getFilmsContainer();
        this.filmListVBox = mainMenu.getFilmListVBox();
        this.scheduleListVBox = mainMenu.getScheduleListVBox();
        this.seatSelectionPane = mainMenu.getSeatSelectionPane();
        this.backButton = mainMenu.getBackButton();
        this.seatGrid = mainMenu.getSeatGrid();
        this.screenLabel = mainMenu.getScreenLabel();
        this.foodDrinkVBox = mainMenu.getFoodDrinkVBox();
        this.foodDrinkTableView = mainMenu.getFoodDrinkTableView();
        this.nameColumn = mainMenu.getNameColumn();
        this.descriptionColumn = mainMenu.getDescriptionColumn();
        this.priceColumn = mainMenu.getPriceColumn();
        this.quantityColumn = mainMenu.getQuantityColumn();
        this.totalColumn = mainMenu.getTotalColumn();
    }

    public void initialize() {
        showAllFilms();


        loadFoodList();  // Đảm bảo rằng foodList đã được tải từ cơ sở dữ liệu
        // Khởi tạo TableView
        initializeTable();
        // Hiển thị các món ăn đã tải lên bảng
        displayFoodOrders();
        // Ẩn các Pane ban đầu
        billPane.setVisible(false);
        seatSelectionPane.setVisible(false);
    }
    @FXML
    public void onBack() {
        // Ẩn Pane hóa đơn khi nhấn Quay lại
        billPane.setVisible(false);

    }

    @FXML
    public void onBackToSchedule() {

        total = 0;
        seatTotalPrice = 0;
        selectedSeats.clear();    // Xóa hết ghế đã chọn

        displayFoodOrders();      // Cập nhật lại TableView đồ ăn

        // Cập nhật hiển thị giao diện
        filmListVBox.setVisible(true);
        scheduleListVBox.setVisible(true);
        seatSelectionPane.setVisible(false);
        paySuccessPane.setVisible(false);
        // Reset nhãn tổng tiền nếu hiển thị ở đây
        totalLabel.setText("Tổng tiền: 0 VNĐ");

    }
    public void onPay() {
        if (selectedFilm != null) {
            String filmName = selectedFilm.getName();  // Lấy tên phim
            String showTime = selectedMovieShow.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " | " + selectedMovieShow.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                    " - " + selectedMovieShow.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));// Cập nhật thông tin thời gian chiếu
            String room = "Phòng " + selectedMovieShow.getRoomId();  // Cập nhật thông tin phòng
            String seat = String.join(", ", selectedSeats);   // Cập nhật thông tin ghế
            String combo = foodOrderList.stream()
                    .filter(foodOrder -> foodOrder.getCount() > 0)  // Chỉ lấy món ăn có số lượng lớn hơn 0
                    .map(foodOrder -> foodOrder.getFood().getName() + " (" + foodOrder.getCount() + ")")  // Lấy tên món ăn và số lượng
                    .collect(Collectors.joining(", "));  // Nối các tên món ăn và số lượng với dấu ", "
            // Nối các tên món ăn và số lượng với dấu ", "
            // Cập nhật combo
            double totalPrice = total;  // Tổng tiền

            // Cập nhật thông tin hóa đơn vào các Label
            filmNameLabel1.setText(filmName);
            showTimeLabel.setText(showTime);
            roomLabel.setText(room);
            seatLabel.setText(seat);
            comboLabel.setText(combo);
            totalLabel.setText(totalPrice + " VNĐ");

            // Hiển thị Pane hóa đơn
            billPane.setVisible(true);
        }
    }
    public void onAppcetPay() {
        try {
            // Kiểm tra xem UserSession đã được tạo chưa
            if (UserSession.getInstance() == null) {
                System.out.println("User session is not available.");
                return; // Nếu session không có, dừng hàm và thông báo lỗi
            }

            // Lấy thông tin người dùng từ UserService
            User Info = UserService.getUser(UserSession.getInstance().getUserId());
            if (Info == null) {
                System.out.println("User information not found.");
                return; // Nếu không tìm thấy thông tin người dùng, dừng hàm
            }

            int userId = Info.getId();
            String billStatus = "PAID";
            Date datePurchased = new Date(System.currentTimeMillis());

            // Tạo đối tượng Bill
            Bill bill = new Bill(userId, datePurchased, billStatus);
            boolean isBillSaved = BillDao.insertBill(bill);

            if (isBillSaved) {
                int billId = bill.getId();

                // Xử lý các ghế đã chọn
                for (String seatPosition : selectedSeats) {
                    Seats seat = SeatDao.getSeatByPositionAndRoom(seatPosition, selectedMovieShow.getRoomId());
                    if (seat == null) continue;

                    SeatType seatType = SeatTypeDao.getSeatTypeById(seat.getSeatTypeId());
                    if (seatType == null) continue;

                    // Tạo đối tượng Reservation
                    Reservation reservation = new Reservation(
                            0,                          // id (auto increment)
                            bill.getId(),               // billId
                            seat.getId(),               // seatId
                            selectedMovieShow.getId(),  // showId
                            seatType.getCost(),         // cost
                            seatType.getName()          // seatTypeName
                    );
                    ReservationDao.insertReservation(reservation);
                }

                // Xử lý các món ăn đã chọn
                for (FoodOrder foodOrder : foodOrderList) {
                    if (foodOrder.getCount() > 0) {
                        // Không cần tính tổng tiền vào cơ sở dữ liệu, chỉ cần lưu các món ăn đã chọn
                        foodOrder.setBillId(billId);
                        FoodOrderDao.insertFoodOrder(foodOrder);
                    }
                }

                // Hiển thị giao diện thanh toán thành công
                paySuccessPane.setVisible(true);
                billPane.setVisible(false);
            } else {
                System.out.println("Thanh toán không thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
            System.out.println("Đã xảy ra lỗi trong quá trình thanh toán.");
        }
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
        selectedFilm = filmDao.getFilmById(filmId);

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
                    selectedMovieShow = show;
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
        seatGrid.setPadding(new Insets(30));
        seatGrid.setHgap(15);
        seatGrid.setVgap(15);
        seatGrid.setAlignment(Pos.CENTER);

        Image seatNormal = new Image(getClass().getResourceAsStream("/Image/ghe thuong.png"));
        Image seatVip = new Image(getClass().getResourceAsStream("/Image/ghe vip.png"));

        List<String> reservedSeats = new ReservationDao().getReservedSeatsByShowId(showId);
        int centerColumn = columns / 2;

        // Thêm header cột
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

                ImageView iv = new ImageView(((columns % 2 == 0 && (j == centerColumn - 1 || j == centerColumn)) ||
                        (columns % 2 != 0 && j == centerColumn)) ? seatVip : seatNormal);
                iv.setFitWidth(50);
                iv.setFitHeight(50);
                iv.setPreserveRatio(true);
                btn.setGraphic(iv);

                boolean vip = ((columns % 2 == 0 && (j == centerColumn - 1 || j == centerColumn)) ||
                        (columns % 2 != 0 && j == centerColumn));
                btn.setStyle(vip
                        ? (reservedSeats.contains(seatName) ? "-fx-background-color: darkred; -fx-border-color: black; -fx-border-radius: 5px;"
                        : "-fx-background-color: gold; -fx-border-color: black; -fx-border-radius: 5px;")
                        : (reservedSeats.contains(seatName) ? "-fx-background-color: red; -fx-border-color: black; -fx-border-radius: 5px;"
                        : "-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 5px;"));

                Tooltip tip = new Tooltip("Ghế " + seatName + (reservedSeats.contains(seatName) ? " - Đã đặt" : ""));
                Tooltip.install(btn, tip);
                btn.setDisable(reservedSeats.contains(seatName));

                btn.setUserData(false);
                final String sName = seatName;
                final boolean isVip = vip;
                btn.setOnAction(e -> {
                    boolean sel = (boolean) btn.getUserData();
                    if (!sel) {
                        selectedSeats.add(sName);
                        seatTotalPrice += isVip ? 100000 : 70000;
                        btn.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-radius: 5px;");
                    } else {
                        selectedSeats.remove(sName);
                        seatTotalPrice -= isVip ? 100000 : 70000;
                        btn.setStyle(isVip
                                ? "-fx-background-color: gold; -fx-border-color: black; -fx-border-radius: 5px;"
                                : "-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 5px;");
                    }
                    btn.setUserData(!sel);
                    updateTotalPrice();
                });

                seatGrid.add(btn, j + 1, i + 1);
            }
        }
    }
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
        priceColumn.setCellValueFactory(param -> new SimpleFloatProperty(param.getValue().getFood().getCost()).asObject());

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
                                updateTotalPrice(foodOrder);
                                updateTotalPrice();// Cập nhật lại tổng tiền
                                displayFoodOrders();  // Cập nhật lại bảng
                            }
                        });

                        // Nút cộng
                        plusButton.setOnAction(event -> {
                            FoodOrder foodOrder = getTableRow().getItem();
                            foodOrder.setCount(foodOrder.getCount() + 1);
                            updateTotalPrice(foodOrder);
                            updateTotalPrice();// Cập nhật lại tổng tiền
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

        totalColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getTotalPrice()).asObject());
    }


    // Cập nhật tổng tiền của món ăn
    private void updateTotalPrice(FoodOrder foodOrder) {
        // Tính toán lại tổng tiền sau khi thay đổi số lượng
        foodOrder.setTotalPrice(foodOrder.getFood().getCost(), foodOrder.getCount());  // Gọi phương thức setTotalPrice đúng cách
    }
    private double total;
    // Cập nhật tổng tiền giỏ hàng và hiển thị lên giao diện
    private void updateTotalPrice() {
        double foodTotalPrice = 0;
        for (FoodOrder foodOrder : foodOrderList) {
            foodTotalPrice += foodOrder.getTotalPrice();  // Tính tổng tiền các món ăn
        }
        total = seatTotalPrice + foodTotalPrice;  // Tổng tiền từ ghế và đồ ăn
        totalLabel.setText("Tổng tiền: " + total + " VNĐ");  // Hiển thị tổng tiền
    }


    // Cập nhật bảng khi có thay đổi
    private void displayFoodOrders() {
        foodOrderObservableList.clear();  // Xóa danh sách hiện tại
        foodOrderObservableList.addAll(foodOrderList);  // Thêm lại danh sách mới
        foodDrinkTableView.setItems(foodOrderObservableList);  // Cập nhật bảng
    }


    public void addFoodToOrder(Food food, int count) {

        // Kiểm tra nếu món ăn đã có trong giỏ hàng
        for (FoodOrder foodOrder : foodOrderList) {
            if (foodOrder.getFood().getId() == food.getId()) {
                foodOrder.setCount(foodOrder.getCount() + count);  // Cập nhật số lượng
                updateTotalPrice(foodOrder);
                updateTotalPrice();// Cập nhật tổng tiền món ăn
                displayFoodOrders();  // Cập nhật lại bảng giỏ hàng
                return;
            }
        }
        // Nếu món ăn chưa có trong giỏ hàng, thêm mới
        FoodOrder foodOrder = new FoodOrder(foodOrderList.size() + 1, food.getId(), 1, count, food);
        foodOrder.setTotalPrice(food.getCost(), count);  // Tính tổng tiền cho món ăn
        foodOrderList.add(foodOrder);  // Thêm món ăn vào giỏ hàng
        displayFoodOrders();  // Cập nhật lại bảng giỏ hàng
    }
}
