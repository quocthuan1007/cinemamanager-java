module CinemaProject {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires java.desktop;

    opens com.utc2.cinema.view to javafx.fxml;
    exports com.utc2.cinema.view;
    exports com.utc2.cinema.controller;
    opens com.utc2.cinema.controller to javafx.fxml;
    opens com.utc2.cinema.model.entity to javafx.base;
}