module CinemaProject {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.utc2.cinema.view to javafx.fxml;
    exports com.utc2.cinema.view;
    exports com.utc2.cinema.controller;
    opens com.utc2.cinema.controller to javafx.fxml;
}