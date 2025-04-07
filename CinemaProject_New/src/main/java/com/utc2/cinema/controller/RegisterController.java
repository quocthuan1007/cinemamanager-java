package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Account;
import com.utc2.cinema.service.AccountService;
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
            // Lấy dữ liệu từ các trường
            String email = emailRegister.getText().trim();
            String password = passwordTf.isVisible() ? passwordTf.getText() : passwordPf.getText();
            String confirmPass = passConfirmTf.isVisible() ? passConfirmTf.getText() : passConfirmPf.getText();

            if (email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                labelError.setText("Vui lòng điền đầy đủ!!");
                return;
            }
            if (!password.equals(confirmPass)) {
                labelError.setText("Mật khẩu không trùng khớp!");
                return;
            }
            //xử lý
            if(AccountService.checkEmail(email) == true)
            {
                labelError.setText("Email tồn tại!");
                return;
            }
            else
            {
                Account accRegis = new Account(0, email, password, "OFF", 3);
                if(AccountService.registerAccount(accRegis))
                {
                    labelError.setText("Thành công");
                }
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
