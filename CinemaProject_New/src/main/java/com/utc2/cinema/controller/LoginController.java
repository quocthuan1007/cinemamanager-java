package com.utc2.cinema.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
        messLogin.setText(userName.getText() + " " + passWord.getText());
    }

    @FXML
    public void onClickRegisterButton(MouseEvent event) {

    }

}
