package com.utc2.cinema.controller;

import com.utc2.cinema.service.AccountService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

    @FXML
    public void onClickLoginButton(ActionEvent event) {
        if(userName.getText() == "" || passWord.getText() == "")
            messLogin.setText("Vui lòng điền đầy đủ thông tin");
        else{
            AccountService account = new AccountService();
            if(account.findAccount(userName.getText(), passWord.getText()) != null)
            {
                messLogin.setText("Đăng nhập thành công");
            }
            else {
                messLogin.setText("Không tìm thấy tài khoản!");
                passWord.setText("");
            }
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
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
