package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Account;
import com.utc2.cinema.model.entity.User;
import com.utc2.cinema.model.entity.UserAccount;
import com.utc2.cinema.model.entity.UserSession;
import com.utc2.cinema.service.AccountService;
import com.utc2.cinema.service.UserService;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManageUserController {

    private TableColumn<UserAccount, String> nameUser;
    private TableColumn<UserAccount, String> numUser;
    private TableColumn<UserAccount, Date> birthUser;
    private TableColumn<UserAccount, String> genderUser;
    private TableColumn<UserAccount, String> emailUser;
    private TableColumn<UserAccount, String> addUser;
    private TableView<UserAccount> tableUser;

    private ObservableList<UserAccount> userAccounts = FXCollections.observableArrayList();

    private ChoiceBox<String> genderConfirm;
    private TextField nameConfirm;
    private TextField numberConfirm;
    private DatePicker birthConfirm;
    private TextField addressConfirm;
    private Pane infoForm;
    private TextField searchEmailField;
    private UserAccount currentSelectedUser;

    public ManageUserController(MainManagerController mainMenu) {
        this.genderConfirm = mainMenu.getGenderConfirm();
        this.nameConfirm = mainMenu.getNameConfirm();
        this.numberConfirm = mainMenu.getNumberConfirm();
        this.birthConfirm = mainMenu.getBirthConfirm();
        this.addressConfirm = mainMenu.getAddressConfirm();
        this.infoForm = mainMenu.getInfoForm();

        this.nameUser = mainMenu.getNameUser();
        this.numUser = mainMenu.getNumUser();
        this.birthUser = mainMenu.getBirthUser();
        this.genderUser = mainMenu.getGenderUser();
        this.emailUser = mainMenu.getEmailUser();
        this.addUser = mainMenu.getAddUser();
        this.tableUser = mainMenu.getTableUser();
        this.searchEmailField = mainMenu.getSearchEmailField();
    }

    public void init()
    {
        nameUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        numUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));
        birthUser.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBirth()));
        genderUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
        emailUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        addUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));

        userAccounts.setAll(getUserAccountList());
        tableUser.setItems(userAccounts);
    }

    private ObservableList<UserAccount> getUserAccountList() {
        ObservableList<UserAccount> list = FXCollections.observableArrayList();
        List<Account> listAccount = AccountService.getAllAccounts();

        for (Account account : listAccount) {
            User user = UserService.getUser(account.getId());
            if (user == null) {
                list.add(new UserAccount(new User(-1, "Null", 3, null, "Null", "Null", account.getId()), account));
            } else {
                String name = user.getName() != null ? user.getName() : "Null";
                String phone = user.getPhone() != null ? user.getPhone() : "Null";
                int gender = user.isGender();
                String address = user.getAddress() != null ? user.getAddress() : "Null";
                Date birth = user.getBirth();

                list.add(new UserAccount(
                        new User(user.getId(), name, gender, birth, phone, address, account.getId()), account));
            }
        }

        return list;
    }

    public void onClickDelete(ActionEvent event) {
        UserAccount select = tableUser.getSelectionModel().getSelectedItem();
        if (select != null) {
            if (AccountService.deleteAccount(select.getAccount()) != 0) {
                UserService.deleteUserByAccountId(select.getAccountId());
                userAccounts.remove(select);
            }
        }
    }

    public void onClickEdit(ActionEvent event) {
        UserAccount select = tableUser.getSelectionModel().getSelectedItem();
        if (select != null && !infoForm.isVisible()) {
            currentSelectedUser = select;
            loadUserInfo(select.getUser());
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), infoForm);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            infoForm.setVisible(true);
        }
    }
    public void onClickView(ActionEvent event)
    {
        UserAccount selected = tableUser.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chưa chọn người dùng");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một người dùng để xem thông tin.");
            alert.showAndWait();
            return;
        }

        User user = selected.getUser();
        Account account = selected.getAccount();

        StringBuilder info = new StringBuilder();
        info.append("Tên: ").append(user.getName()).append("\n");
        info.append("SĐT: ").append(user.getPhone()).append("\n");
        info.append("Giới tính: ").append(user.isGender() == 0 ? "Nam" : "Nữ").append("\n");
        info.append("Ngày sinh: ").append(user.getBirth()).append("\n");
        info.append("Địa chỉ: ").append(user.getAddress()).append("\n");
        info.append("Email: ").append(account.getEmail());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông tin người dùng");
        alert.setHeaderText("Chi tiết người dùng");
        alert.setContentText(info.toString());
        alert.showAndWait();
    }
    public void setupGenderComboBox() {
        if (genderConfirm.getItems().isEmpty()) {
            genderConfirm.getItems().addAll("Nam", "Nữ");
        }
    }

    private void clearInfoInput() {
        nameConfirm.clear();
        birthConfirm.setValue(null);
        genderConfirm.setValue(null);
        addressConfirm.clear();
        numberConfirm.clear();
    }

    private void loadUserInfo(User info) {
        if (info == null) {
            nameConfirm.setText("Null");
            birthConfirm.setValue(null);
            genderConfirm.setValue(null);
            addressConfirm.setText("Null");
            numberConfirm.setText("Null");
        } else {
            nameConfirm.setText(info.getName() != null ? info.getName() : "Null");

            Date birthDate = info.getBirth();
            if (birthDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(birthDate);
                LocalDate localDate = LocalDate.of(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                birthConfirm.setValue(localDate);
            } else {
                birthConfirm.setValue(null);
            }

            addressConfirm.setText(info.getAddress() != null ? info.getAddress() : "Null");
            numberConfirm.setText(info.getPhone() != null ? info.getPhone() : "Null");
            genderConfirm.setValue(info.isGender() == 0 ? "Nam" : "Nữ");
        }
    }

    public void onPlayerCloseInfoConfirm(ActionEvent event) {
        if (infoForm.isVisible()) {
            infoForm.setVisible(false);
            currentSelectedUser = null;
        }
    }

    public void onPlayerSaveInfoConfirm(ActionEvent event) {
        try {
            String name = nameConfirm.getText();
            String genderText = genderConfirm.getValue();
            LocalDate birthValue = birthConfirm.getValue();
            String address = addressConfirm.getText();
            String phone = numberConfirm.getText();

            if (name.isEmpty() || genderText == null || birthValue == null || address.isEmpty() || phone.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng điền đầy đủ tất cả các thông tin trước khi lưu.");
                return;
            }

            int gender = genderText.equals("Nam") ? 0 : 1;
            Date birth = java.sql.Date.valueOf(birthValue);

            int accountId = (currentSelectedUser != null) ? currentSelectedUser.getAccountId() : UserSession.getInstance().getUserId();
            User user = new User(0, name, gender, birth, phone, address, accountId);

            if (UserService.getUser(accountId) != null) {
                UserService.updateUser(user);
            } else {
                UserService.insertUser(user);
            }

            showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Thông tin đã được lưu thành công!");

            userAccounts.setAll(getUserAccountList());
            infoForm.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lưu thông tin thất bại. Vui lòng kiểm tra lại dữ liệu.");
        }
    }

    public void onSearchByEmail() {
        String keyword = searchEmailField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            tableUser.setItems(userAccounts); // Hiển thị lại toàn bộ danh sách
            return;
        }

        ObservableList<UserAccount> filteredList = FXCollections.observableArrayList();

        for (UserAccount ua : userAccounts) {
            if (ua.getEmail().toLowerCase().contains(keyword)) {
                filteredList.add(ua);
            }
        }

        tableUser.setItems(filteredList);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
