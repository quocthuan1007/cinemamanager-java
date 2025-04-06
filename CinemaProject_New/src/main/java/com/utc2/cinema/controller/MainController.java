package com.utc2.cinema.controller;

import com.utc2.cinema.model.entity.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;

public class MainController implements Initializable {

    @FXML
    private Label buyBtn;

    @FXML
    private Pane buyForm;

    @FXML
    private Label introBtn;

    @FXML
    private Pane introForm;

    @FXML
    private Label mainMenuBtn;

    @FXML
    private Pane mainMenuForm;

    @FXML
    private Label movieBtn;

    @FXML
    private Pane movieForm;

    @FXML
    private Label scheduleBtn;

    @FXML
    private Pane scheduleForm;

    public Label getUserMain() {
        return userMain;
    }

    @FXML
    private Label userMain;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        mainMenuForm.setVisible(true);
        introForm.setVisible(false);
        movieForm.setVisible(false);
        scheduleForm.setVisible(false);
        buyForm.setVisible(false);

        if (UserSession.getInstance() != null) {
            String email = UserSession.getInstance().getEmail();
            userMain.setText(email);
        } else {
            userMain.setText("No user.");
        }
    }

    @FXML
    void switchButton(MouseEvent event) {
        if(event.getSource() == mainMenuBtn)
        {
            mainMenuForm.setVisible(true);
            introForm.setVisible(false);
            movieForm.setVisible(false);
            scheduleForm.setVisible(false);
            buyForm.setVisible(false);
        }
        else if(event.getSource() == movieBtn)
        {
            movieForm.setVisible(true);
            mainMenuForm.setVisible(false);
            introForm.setVisible(false);
            scheduleForm.setVisible(false);
            buyForm.setVisible(false);
        }
        else if(event.getSource() == scheduleBtn)
        {
            scheduleForm.setVisible(true);
            movieForm.setVisible(false);
            mainMenuForm.setVisible(false);
            introForm.setVisible(false);
            buyForm.setVisible(false);
        }
        else if(event.getSource() == buyBtn)
        {
            buyForm.setVisible(true);
            movieForm.setVisible(false);
            mainMenuForm.setVisible(false);
            introForm.setVisible(false);
            scheduleForm.setVisible(false);
        }
        else if(event.getSource() == introBtn)
        {
            introForm.setVisible(true);
            movieForm.setVisible(false);
            mainMenuForm.setVisible(false);
            scheduleForm.setVisible(false);
            buyForm.setVisible(false);
        }
    }

}
