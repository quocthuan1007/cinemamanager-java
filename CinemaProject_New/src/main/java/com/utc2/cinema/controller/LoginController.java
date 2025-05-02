package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.Account;
import com.utc2.cinema.model.entity.CustomAlert;
import com.utc2.cinema.model.entity.UserSession;
import com.utc2.cinema.service.AccountService;
import com.utc2.cinema.utils.PasswordUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button loginButton;
    @FXML
    private Label messLogin;
    @FXML
    private PasswordField passWord;
    @FXML
    private Label registerButton;
    @FXML
    private TextField userName;

    private void showMainMenu()
    {
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
    private void showManagerMenu()
    {
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
    public void onClickLoginButton(ActionEvent event)
    {
        try {
            if (userName.getText() == "" || passWord.getText() == "")
                messLogin.setText("Vui lòng điền đầy đủ thông tin");
            else {
                if (PasswordUtils.checkPassword(passWord.getText(), AccountService.getPassword(userName.getText()))) {
                    Account findAccount = AccountService.findAccount(userName.getText());
                    if (findAccount != null) {
                        UserSession.createUserSession(findAccount.getId(), findAccount.getEmail(), findAccount.getPassword(), findAccount.getAccountStatus(), findAccount.getRoleId());
                        userName.setText("");
                        passWord.setText("");
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
        }
        catch (Exception e)
        {
            CustomAlert.showError("", "Có lỗi xảy ra", "Không thể đăng nhập!");
        }
    }
    @FXML
    void onMouseClickEmail(MouseEvent   event) {
        if(messLogin.getText() != "")
            messLogin.setText("");
    }

    @FXML
    void onMouseClickedPassword(MouseEvent   event) {
        if(messLogin.getText() != "")
            messLogin.setText("");
    }
    @FXML
    public void onClickRegisterButton(MouseEvent event)
    {
        Stage loginWin = (Stage) userName.getScene().getWindow();
        loginWin.close();

        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/FXML/Register.fxml"));
        RegisterController control = new RegisterController();
        loader.setController(control);
        try {
            Pane root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Register !");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
