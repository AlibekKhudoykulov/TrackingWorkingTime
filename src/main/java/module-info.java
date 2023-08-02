module com.example.trackinghours {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    opens com.example.trackinghours to javafx.fxml;
    exports com.example.trackinghours;
    exports com.example.trackinghours.controller;
    opens com.example.trackinghours.controller to javafx.fxml;
}