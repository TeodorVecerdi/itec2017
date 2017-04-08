package com.powersoft.itec2017.window.controller;

import com.powersoft.itec2017.alerts.AddLocationAlert;
import com.powersoft.itec2017.framework.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class ManageLocationsController implements Initializable {
    public TextField startDateFilter;
    public TextField endDateFilter;
    public WebView webView;
    public TableView tableView;
    public TableColumn locationColumn;
    public TableColumn startDateColumn;
    public TableColumn endDateColumn;
    public TableColumn priceColumn;

    Stage stage = new Stage();
    String username;

    public ManageLocationsController(String username) {
        this.username = username;
    }

    public void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ManageLocationsWindow.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        locationColumn.setCellValueFactory(new PropertyValueFactory<LocationMaster, String>("location"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<LocationMaster, String>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<LocationMaster, String>("endDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<LocationMaster, String>("price"));
    }


    public void updateMap(ActionEvent actionEvent) {
        update();
    }

    public void update(){
        String startDate = startDateFilter.getText();
        String endDate = endDateFilter.getText();
        updateTable(startDate, endDate);
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(MapManager.getMap(startDate, endDate, username));
    }

    public void createEntry(ActionEvent actionEvent) {
        String result = AddLocationAlert.display();
        if (result != null) {
            String[] splitResult = result.split(";");
            String addLocation = "INSERT INTO " + username + "(LocationName, StartDate, EndDate, Price) VALUES (?, ?, ?, ?)";
            DatabaseManager.executePreparedUpdate(addLocation, splitResult[0], splitResult[1], splitResult[2], splitResult[3]);
        }
        update();
    }


    public void editEntry(ActionEvent actionEvent) {
    }

    public void deleteSelected(ActionEvent actionEvent) {
        LocationMaster locationMaster = ((LocationMaster) tableView.getSelectionModel().getSelectedItem());
        String sql = "DELETE FROM " + username + " WHERE LocationName = '" + locationMaster.getLocation() + "' AND StartDate = '" + locationMaster.getStartDate() + "' AND EndDate = '" + locationMaster.getEndDate() + "' AND Price = '" + locationMaster.getPrice() + "'";
        DatabaseManager.executeUpdate(sql);

        update();
    }

    public void updateTable(String startDate, String endDate) {
        String sql = "SELECT * FROM " + username;
        ResultSet resultSet = DatabaseManager.executeQuery(sql);
        boolean filter = false;
        if (!startDate.isEmpty() && !endDate.isEmpty())
            filter = true;
        try {
            ObservableList<LocationMaster> data = FXCollections.observableArrayList();
            while (resultSet.next()) {
                boolean isOk = true;
                String compStartDate = resultSet.getString("StartDate");
                String compEndDate = resultSet.getString("EndDate");
                if (filter)
                    isOk = Period.isWithinPeriod(startDate, endDate, compStartDate, compEndDate);
                if (isOk) {
                    LocationMaster item = new LocationMaster();
                    item.setLocation(resultSet.getString("LocationName"));
                    item.setStartDate(compStartDate);
                    item.setEndDate(compEndDate);
                    item.setPrice(resultSet.getString("Price"));
                    System.out.println(item);
                    data.add(item);
                }
                tableView.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void funcExit(ActionEvent actionEvent) {
        ((Stage) (tableView.getScene().getWindow())).close();
    }

    public void sendMail(ActionEvent actionEvent) {
        System.out.println("TODO sendMail");
        ((Stage) (tableView.getScene().getWindow())).close();
    }

    public void exportReport(ActionEvent actionEvent) {
        System.out.println("TODO exportReport");
        ((Stage) (tableView.getScene().getWindow())).close();
    }

    @SuppressWarnings("Duplicates")
    public void funcLogOut(ActionEvent actionEvent) {
        try {
            Parent loginParent = FXMLLoader.load(getClass().getResource("../fxml/LoginWindow.fxml"));
            Scene scene = new Scene(loginParent);
            scene.getStylesheets().addAll("styles/glyphs_blue.css");
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void funcSettings(ActionEvent actionEvent) {

    }

    public void funcImport(ActionEvent actionEvent) {
        FileManager.importData();

    }

    public void funcExport(ActionEvent actionEvent) {
        FileManager.exportData(username);
    }


}
