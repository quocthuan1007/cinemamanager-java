module CinemaProject {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jbcrypt;
    requires javafx.swing;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    opens com.utc2.cinema.view to javafx.fxml;
    exports com.utc2.cinema.view;
    exports com.utc2.cinema.controller;
    opens com.utc2.cinema.controller to javafx.fxml;
    exports com.utc2.cinema.model.entity;
    opens com.utc2.cinema.model.entity to javafx.base, javafx.fxml;
    exports com.utc2.cinema.utils;
    opens com.utc2.cinema.utils to javafx.fxml;
}