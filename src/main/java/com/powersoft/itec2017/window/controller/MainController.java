package com.powersoft.itec2017.window.controller;

import com.powersoft.itec2017.Main;
import com.powersoft.itec2017.alerts.AddLocationAlert;
import com.powersoft.itec2017.framework.DatabaseManager;
import com.powersoft.itec2017.framework.FileManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public Button addLocationButton;
    public Button manageAccountButton;
    public WebView webView;
    public Text firstNameText;
    public Text lastNameText;
    public Text usernameText;
    public Text homeLocationText;

    public String username, firstName, lastName, homeLocation;


    public MainController(String username) {
        this.username = username;
        try {
            ResultSet resultSet = DatabaseManager.executeQuery("SELECT FirstName, LastName, City FROM accounts WHERE username = '" + username + "'");
            resultSet.next();
            this.firstName = resultSet.getString("FirstName");
            this.lastName = resultSet.getString("LastName");
            this.homeLocation = resultSet.getString("City");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void funcExit(ActionEvent actionEvent) {
        ((Stage)(usernameText.getScene().getWindow())).close();
    }

    public void funcLogOut(ActionEvent actionEvent) {
        try {
            Parent loginParent = FXMLLoader.load(getClass().getResource("../fxml/LoginWindow.fxml"));
            Scene scene = new Scene(loginParent);
            scene.getStylesheets().addAll( "styles/glyphs_blue.css");
            Stage stage = (Stage) usernameText.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void funcSettings(ActionEvent actionEvent) {

    }

    public void funcImport(ActionEvent actionEvent) {
        FileManager.importData();
        updateMap();
    }

    public void funcExport(ActionEvent actionEvent) {
        FileManager.exportData(username);
    }

    public void funcMapFullscreen(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        firstNameText.setText(firstNameText.getText() + firstName);
        lastNameText.setText(lastNameText.getText() + lastName);
        usernameText.setText(usernameText.getText() + username);
        homeLocationText.setText(homeLocationText.getText() + homeLocation);
        updateMap();
    }

    public String loadMap(String homeLocation, String... orase) {
        String center = "Europe";
        int zoom = 3, w = 1340,  h = 400;
        String color = null;
        switch (homeLocation){
            case "Timisoara": {
                color = "red";
                break;
            }
            case "Bucuresti": {
                color = "blue";
                break;
            }
            case "Brasov": {
                color = "green";
                break;
            }
            case "Sibiu": {
                color = "pink";
                break;
            }
        }
        Random random = new Random();
        String result = "https://maps.googleapis.com/maps/api/staticmap?center=" + center + "&zoom=" + zoom + "&size=" + w + "x" + h + "&maptype=roadmap";
        for (String oras : orase) {
            oras = oras.replace(' ', '+');
            result += "&markers=color:" + color + "%7Clabel:%7C" + oras;
        }
        result += "&key=AIzaSyAyQVIR0G0-GZLHVOBvqs7wj7O-YSgM7Mg";
        return result;
    }

    public void addLocation(Event event) {
        String result = AddLocationAlert.display();
        if(result != null) {
            String [] splitResult = result.split(";");
            String addLocation = "INSERT INTO " + username + "(LocationName, StartDate, EndDate, Price) VALUES (?, ?, ?, ?)";
            try {
                DatabaseManager.executePreparedUpdate(addLocation, splitResult[0], splitResult[1], splitResult[2]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        updateMap();
    }

    private void updateMap() {
        String selectLocations = "SELECT * FROM " + username;
        try {
            ResultSet resultSet = DatabaseManager.executeQuery(selectLocations);
            List<String> orase = new ArrayList<>();
            while(resultSet.next()){
                orase.add(resultSet.getString("LocationName"));
            }

            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent("<center><img src=\"" + loadMap(homeLocation, orase.toArray(new String[orase.size()])) + "\"/></center>");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void manageAccount(Event event) {

    }

}
