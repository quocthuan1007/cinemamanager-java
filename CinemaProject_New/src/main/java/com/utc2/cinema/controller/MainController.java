package com.utc2.cinema.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;

public class MainController {

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
