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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RegisterController
{

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
            Stage register = (Stage) backButton.getScene().getWindow();
            register.close();
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
            } catch (Exception e) {
                e.printStackTrace();
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
