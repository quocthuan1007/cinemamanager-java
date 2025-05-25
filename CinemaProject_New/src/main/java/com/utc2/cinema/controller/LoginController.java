package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Account;
import com.utc2.cinema.model.entity.CustomAlert;
import com.utc2.cinema.model.entity.UserSession;
import com.utc2.cinema.service.AccountService;
import com.utc2.cinema.utils.PasswordUtils;
import com.utc2.cinema.utils.ValidationUtils;
import com.utc2.cinema.view.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button loginButton;
    @FXML
    private PasswordField passWord;
    @FXML
    private Label toRegisterButton;
    @FXML
    private TextField userName;
    @FXML
    private TextField passWordText;
    @FXML
    private CheckBox showPass;

    private void showMainMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/FXML/MainMenu.fxml"));
            Pane root = fxmlLoader.load();
            Scene scene = new Scene(root, 1160, 800);
            Stage stage = new Stage();
            stage.setTitle("Cinema Manager");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showManagerMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/FXML/MainManager.fxml"));
            MainManagerController a = new MainManagerController();
            fxmlLoader.setController(a);
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root, 1160, 800);
            Stage stage = new Stage();
            stage.setTitle("Cinema Manager");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickLoginButton(ActionEvent event) {
        try {
            String passw = passWord.isVisible() ? passWord.getText() : passWordText.getText();
            if (userName.getText() == "" || passw == "")
                CustomAlert.showError("", "Có lỗi", "Vui lòng điền đầy đủ");
            else {
                String pass = passWord.isVisible() ? passWord.getText() : passWordText.getText();
                if (PasswordUtils.checkPassword(pass, AccountService.getPassword(userName.getText()))) {
                    Account findAccount = AccountService.findAccount(userName.getText());
                    if (findAccount != null) {
                        UserSession.createUserSession(findAccount.getId(), findAccount.getEmail(), findAccount.getPassword(), findAccount.getAccountStatus(), findAccount.getRoleId());
                        userName.clear();
                        passWord.clear();
                        passWordText.clear();
                        CustomAlert.showInfo("", "Thành công", "Đăng nhập thành công");
                        Stage loginWin = (Stage) userName.getScene().getWindow();
                        loginWin.close();
                        if (UserSession.getInstance().getRoleId() == 1)
                            showManagerMenu();
                        else if (UserSession.getInstance().getRoleId() == 3) {
                            showMainMenu();
                        }
                    }
                } else {
                    CustomAlert.showError("", "Có lỗi xảy ra", "Không tìm thấy tài khoản!");
                    passWord.setText("");
                }
            }
        } catch (Exception e) {
            CustomAlert.showError("", "Có lỗi xảy ra", "Không thể đăng nhập!");
        }
    }

    @FXML
    void onCloseLogin(MouseEvent e) {
        Stage stage = (Stage) userName.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onMinLogin(MouseEvent e) {
        Stage stage = (Stage) userName.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void onMouseClickEmail(MouseEvent   event) {

    }

    @FXML
    void onMouseClickedPassword(MouseEvent   event) {

    }
    @FXML
    void onClickShowPassLogin(ActionEvent event) {
        if(showPass.isSelected() == true)
        {
            passWord.setVisible(false);
            passWordText.setVisible(true);
            passWordText.setText(passWord.getText());
        }
        else
        {
            passWord.setVisible(true);
            passWordText.setVisible(false);
            passWord.setText(passWordText.getText());
        }
    }
    @FXML
    public void onClickToRegister(MouseEvent event)
    {
        if(loginPane.isVisible()) {
            loginPane.setVisible(false);
            registerPane.setVisible(true);
        }
    }
    @FXML
    private Label backButton;

    @FXML
    private Label labelError;

    @FXML
    private TextField emailRegister;

    @FXML
    private TextField passConfirmPf;

    @FXML
    private PasswordField passConfirmTf;

    @FXML
    private TextField passwordPf;

    @FXML
    private PasswordField passwordTf;

    @FXML
    private Button registerButton;

    @FXML
    private CheckBox showPassCF;
    @FXML
    private Pane loginPane;
    @FXML
    private Pane registerPane;

    @FXML
    void OnClickPassConfirmPf(MouseEvent event) {
        if(labelError.getText() != "")
            labelError.setText("");
    }

    @FXML
    void OnClickPassConfirmTf(MouseEvent event) {
        if(labelError.getText() != "")
            labelError.setText("");
    }

    @FXML
    void OnClickPassPf(MouseEvent event) {
        if(labelError.getText() != "")
            labelError.setText("");
    }

    @FXML
    void OnClickPassTf(MouseEvent event) {
        if(labelError.getText() != "")
            labelError.setText("");
    }

    @FXML
    void onClickBackButton(MouseEvent event)
    {
        if(registerPane.isVisible()) {
            registerPane.setVisible(false);
            loginPane.setVisible(true);
        }
    }

    @FXML
    void onClickRegisterButton(ActionEvent event) {
        String email = emailRegister.getText().trim();
        String password = passwordTf.isVisible() ? passwordTf.getText() : passwordPf.getText();
        String confirmPass = passConfirmTf.isVisible() ? passConfirmTf.getText() : passConfirmPf.getText();

        if (email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            CustomAlert.showError("", "Có lỗi xảy ra", "Vui lòng điền đầy đủ!");
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            CustomAlert.showError("", "Có lỗi xảy ra", "Email không đúng định dạng (thuan@gmail.com)!");
            return;
        }
        if (!PasswordUtils.isValidPassword(password)) {
            CustomAlert.showError("", "Có lỗi xảy ra",
                    "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt!");
            return;
        }
        if (!password.equals(confirmPass)) {
            CustomAlert.showError("", "Có lỗi xảy ra", "Mật khẩu không trùng khớp!");
            return;
        }
        if (AccountService.checkEmail(email)) {
            CustomAlert.showError("", "Có lỗi xảy ra", "Email này đã tồn tại!");
            return;
        }
        Account accRegis = new Account(0, email, PasswordUtils.hashPassword(password), "OFFLINE", 3);
        emailRegister.clear();
        passwordTf.clear();
        passwordPf.clear();
        passConfirmTf.clear();
        passConfirmPf.clear();
        if (AccountService.registerAccount(accRegis)) {
            boolean check = CustomAlert.showConfirmation("","Hoàn tất","Đăng ký thành công, bạn có muốn đăng nhập không?");
            if(check == true)
            {
                try {
                    Account findAccount = AccountService.findAccount(accRegis.getEmail());
                    UserSession.createUserSession(findAccount.getId(),findAccount.getEmail(),findAccount.getPassword(),findAccount.getAccountStatus(),findAccount.getRoleId());

                    Stage loginWin = (Stage) emailRegister.getScene().getWindow();
                    loginWin.close();

                    FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/FXML/MainMenu.fxml"));
                    Pane root = fxmlLoader.load();
                    Scene scene = new Scene(root, 1160, 800);
                    Stage stage = new Stage();
                    stage.setTitle("Cinema Manager");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            CustomAlert.showError("", "Có lỗi xảy ra", "Đăng ký thất bại!");
        }
    }


    @FXML
    void onClickShowPass(ActionEvent event) {
        boolean show = showPassCF.isSelected();
        handlePassword(show);
    }
    public void handlePassword(boolean show) {
        if (show) {
            passwordPf.setText(passwordTf.getText());
            passwordTf.setVisible(false);
            passwordPf.setVisible(true);
            passConfirmPf.setText(passConfirmTf.getText());
            passConfirmTf.setVisible(false);
            passConfirmPf.setVisible(true);
        } else
        {
            passwordTf.setText(passwordPf.getText());
            passwordPf.setVisible(false);
            passwordTf.setVisible(true);
            passConfirmTf.setText(passConfirmPf.getText());
            passConfirmPf.setVisible(false);
            passConfirmTf.setVisible(true);
        }
    }

    @FXML
    void onMouseClickedEmail(MouseEvent event) {
        if(labelError.getText() != "")
            labelError.setText("");
    }

}
