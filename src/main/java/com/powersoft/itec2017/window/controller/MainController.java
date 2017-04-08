package com.powersoft.itec2017.window.controller;

import com.powersoft.itec2017.alerts.AddLocationAlert;
import com.powersoft.itec2017.framework.DatabaseManager;
import com.powersoft.itec2017.framework.FileManager;
import com.powersoft.itec2017.framework.MapManager;
import com.powersoft.itec2017.framework.ReportTravel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public Button addLocationButton;
    public Button manageAccountButton;
    public WebView webView = new WebView();
    public Text firstNameText;
    public Text lastNameText;
    public Text emailText;
    public Text homeLocationText;

    public String username, firstName, lastName, homeLocation, email;


    public MainController(String username) {
        this.username = username;
        try {
            ResultSet resultSet = DatabaseManager.executeQuery("SELECT FirstName, LastName, City, Email FROM accounts WHERE username = '" + username + "'");
            resultSet.next();
            this.firstName = resultSet.getString("FirstName");
            this.lastName = resultSet.getString("LastName");
            this.homeLocation = resultSet.getString("City");
            this.email = resultSet.getString("Email");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void funcExit(ActionEvent actionEvent) {
        ((Stage) (lastNameText.getScene().getWindow())).close();
    }

    public void sendMail(ActionEvent actionEvent) {
        System.out.println("TODO sendMail");
        ((Stage) (lastNameText.getScene().getWindow())).close();
    }

    public void exportReport(ActionEvent actionEvent) {
        ReportTravel.exelReport(username);
    }

    public void funcLogOut(ActionEvent actionEvent) {
        try {
            Parent loginParent = FXMLLoader.load(getClass().getResource("../fxml/LoginWindow.fxml"));
            Scene scene = new Scene(loginParent);
            scene.getStylesheets().addAll("styles/glyphs_blue.css");
            Stage stage = (Stage) lastNameText.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void funcSettings(ActionEvent actionEvent) {

    }

    public void funcImport(ActionEvent actionEvent) {
        FileManager.importData();
//        updateMap();
    }

    public void funcExport(ActionEvent actionEvent) {
        FileManager.exportData(username);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        firstNameText.setText(firstNameText.getText() + firstName);
        lastNameText.setText(lastNameText.getText() + lastName);
        emailText.setText(emailText.getText() + email);
        homeLocationText.setText(homeLocationText.getText() + homeLocation);
//        updateMap();
    }


    public void addLocation(Event event) {
        String result = AddLocationAlert.display();
        if (result != null) {
            String[] splitResult = result.split(";");
            String addLocation = "INSERT INTO " + username + "(LocationName, StartDate, EndDate, Price) VALUES (?, ?, ?, ?)";
            DatabaseManager.executePreparedUpdate(addLocation, splitResult[0], splitResult[1], splitResult[2], splitResult[3]);
        }
    }



    public void manageAccount(Event event) {
        new ManageAccountController(username).display();
    }

    public void manageLocations(Event event) {
        new ManageLocationsController(username).display();
    }

}
