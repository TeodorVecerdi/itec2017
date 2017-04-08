package com.powersoft.itec2017.window.controller;

import com.powersoft.itec2017.window.controller.admin.AdminUserManager;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AdminManagerController {
    String username;

    Stage stage = new Stage();

    public AdminManagerController(String username) {
        this.username = username;
    }
    public void display() {
        stage.setTitle("Admin Panel");

        // TODO: 4/8/2017 creare/stergere/editare parola -> useri
        // TODO: 4/8/2017 harta 1+ useri pe o perioada
        // TODO: 4/8/2017 calatorii 1+ utilizatori pe o perioada

        VBox layout = new VBox(10);
        //1.
        Button userManager = new Button("User Manager");
        Button mapManager = new Button("Map Manager");
        Button locationManager = new Button("Location Manager");

        layout.getChildren().addAll(userManager, mapManager, locationManager);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();

        userManager.setOnAction(event -> {
            new AdminUserManager().display();
//            stage.close();
        });
        mapManager.setOnAction(event -> {
//            new AdminMapManager().display();
//            stage.close();
        });
        locationManager.setOnAction(event -> {
//            new AdminLocationManager().display();
//            stage.close();
        });

    }
}
