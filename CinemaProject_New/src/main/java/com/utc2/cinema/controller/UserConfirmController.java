package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.CustomAlert;
import com.utc2.cinema.model.entity.User;
import com.utc2.cinema.model.entity.UserSession;
import com.utc2.cinema.service.UserService;
import com.utc2.cinema.utils.ValidationUtils;
import com.utc2.cinema.view.Login;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;

public class UserConfirmController {
    private Label userMain;
    private ChoiceBox<String> genderConfirm;
    private TextField nameConfirm;
    private TextField numberConfirm;
    private DatePicker birthConfirm;
    private TextField addressConfirm;
    private Pane infoForm;
    public UserConfirmController(MainMenuController mainMenu)
    {
        this.userMain = mainMenu.getUserMain();
        this.genderConfirm = mainMenu.getGenderConfirm();
        this.nameConfirm = mainMenu.getNameConfirm();
        this.numberConfirm = mainMenu.getNumberConfirm();
        this.birthConfirm = mainMenu.getBirthConfirm();
        this.addressConfirm = mainMenu.getAddressConfirm();
        this.infoForm = mainMenu.getInfoContainer();
    }
    public void setupUser() {
        if (UserSession.getInstance() != null) {
            String email = UserSession.getInstance().getEmail();
            userMain.setText(email);
        } else {
            userMain.setText("No user.");
        }
    }

    public void setupGenderComboBox() {
        if (genderConfirm.getItems().isEmpty()) {
            genderConfirm.getItems().addAll("Nam", "Nữ");
        }
    }
    private void clearInfoInput()
    {
        nameConfirm.setText("");
        birthConfirm.setValue(null);
        genderConfirm.setValue(null);
        addressConfirm.setText("");
        numberConfirm.setText("");
    }
    private void loadUserInfo()
    {
        User Info = UserService.getUser(UserSession.getInstance().getUserId());
        if(Info == null)
        {
            nameConfirm.setText("Null");
            birthConfirm.setValue(null);
            genderConfirm.setValue(null);
            addressConfirm.setText("Null");
            numberConfirm.setText("Null");
        }
        else {
            nameConfirm.setText(Info.getName() != null ? Info.getName() : "Null");
            Date birthDate = Info.getBirth();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birthDate);
            LocalDate localDate = LocalDate.of(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            birthConfirm.setValue(localDate);
            addressConfirm.setText(Info.getAddress() != null ? Info.getAddress() : "Null");
            numberConfirm.setText(Info.getPhone() != null ? Info.getPhone() : "Null");
            genderConfirm.setValue(Info.isGender() == 0 ? "Nam" : "Nữ");
        }
    }
    private ContextMenu infoMenu = new ContextMenu();

    public void onPlayerClickInfoConfirm(MouseEvent event) {
        if (infoMenu.isShowing()) {
            infoMenu.hide();
            return;
        }

        infoMenu.getItems().clear();
        Label editLabel = new Label("✏️  Thông tin");
        Label logoutLabel = new Label("🚪  Đăng xuất");

        String labelStyle = "-fx-padding: 8 16; -fx-font-size: 14; -fx-text-fill: #333;";
        editLabel.setStyle(labelStyle);
        logoutLabel.setStyle(labelStyle);

        CustomMenuItem editItem = new CustomMenuItem(editLabel, true);
        CustomMenuItem logoutItem = new CustomMenuItem(logoutLabel, true);

        editLabel.setOnMouseClicked(e -> {
            if (!infoForm.isVisible()) {
                loadUserInfo();
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), infoForm);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
                infoForm.setVisible(true);
            }
            infoMenu.hide();
        });

        logoutLabel.setOnMouseClicked(e -> {
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.close();
            UserSession.getInstance().cleanUserSession();
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("/FXML/Login.fxml"));
                LoginController control = new LoginController();
                fxmlLoader.setController(control);
                Pane root = fxmlLoader.load();
                Scene scene = new Scene(root, 600, 400);
                stage.setTitle("Hello !");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            infoMenu.hide();
        });

        infoMenu.getItems().addAll(editItem, logoutItem);
        infoMenu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
    }




    public void onPlayerCloseInfoConfirm(ActionEvent event) {
        if(infoForm.isVisible())
            infoForm.setVisible(false);
    }

    public void onPlayerSaveInfoConfirm(ActionEvent event) {
        try {
            String name = nameConfirm.getText();
            String genderText = genderConfirm.getValue();
            java.time.LocalDate birthValue = birthConfirm.getValue(); // lấy trước
            String address = addressConfirm.getText();
            String phone = numberConfirm.getText();

            if (name == null || name.trim().isEmpty()) {
                CustomAlert.showError("", "Lỗi nhập liệu", "Tên không được để trống hoặc chỉ chứa khoảng trắng.");
                return;
            }

            if (genderText == null) {
                CustomAlert.showError("", "Lỗi nhập liệu", "Vui lòng chọn giới tính.");
                return;
            }

            if (birthValue == null) {
                CustomAlert.showError("", "Lỗi nhập liệu", "Vui lòng chọn ngày sinh.");
                return;
            }

            if (birthValue.isAfter(LocalDate.now()) || birthValue.isBefore(LocalDate.of(1900, 1, 1))) {
                CustomAlert.showError("", "Lỗi nhập liệu", "Ngày sinh không hợp lệ. Vui lòng chọn trong khoảng từ năm 1900 đến hiện tại.");
                return;
            }

            if (address == null || address.trim().isEmpty()) {
                CustomAlert.showError("", "Lỗi nhập liệu", "Địa chỉ không được để trống.");
                return;
            }

            if (!ValidationUtils.isValidPhone(phone)) {
                CustomAlert.showError("", "Lỗi nhập liệu", "Số điện thoại không hợp lệ. Phải bắt đầu bằng số 0 và gồm 10 chữ số.");
                return;
            }


            int gender = genderText.equals("Nam") ? 0 : 1;
            java.util.Date birth = java.sql.Date.valueOf(birthValue);

            int accountId = UserSession.getInstance().getUserId();
            User user = new User(0, name, gender, birth, phone, address, accountId);

            if (UserService.getUser(accountId) != null) {
                UserService.updateUser(user);
            } else {
                UserService.insertUser(user);
            }

            CustomAlert.showInfo("","Hoàn tất", "Thông tin đã được lưu.");

            clearInfoInput();
            loadUserInfo();

        } catch (Exception e) {
            e.printStackTrace();
            CustomAlert.showError("","Có lỗi xảy ra!", "Lưu thất bại!");
        }
    }
}
