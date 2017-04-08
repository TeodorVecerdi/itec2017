package com.powersoft.itec2017.window.controller.admin;

import com.powersoft.itec2017.framework.DatabaseManager;
import com.powersoft.itec2017.framework.LocationMaster;
import com.powersoft.itec2017.framework.UserMaster;
import com.powersoft.itec2017.window.controller.ManageAccountController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminUserManager implements Initializable{

    public TableColumn usernameColumn;
    public TableColumn firstNameColumn;
    public TableColumn lastNameColumn;
    public TableColumn emailColumn;
    public TableView tableView;
    Stage stage;
    public void display() {
        stage = new Stage();
        stage.setTitle("Admin User Manager");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../../fxml/admin/AdminUserManager.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<UserMaster, String>("username"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<UserMaster, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<UserMaster, String>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<UserMaster, String>("email"));
    }

    public void createUser(ActionEvent actionEvent) {
        try {
            Parent register = FXMLLoader.load(getClass().getResource("../fxml/RegisterWindow.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(register);
            scene.getStylesheets().addAll("styles/glyphs_blue.css");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(ActionEvent actionEvent) {
        UserMaster user = ((UserMaster) tableView.getSelectionModel().getSelectedItem());
        String sql = "DELETE FROM accounts WHERE Username = '" + user.getUsername() + "'";
        DatabaseManager.executeUpdate(sql);
        updateTable();
    }

    public void editUser(ActionEvent actionEvent) {
        UserMaster user = ((UserMaster) tableView.getSelectionModel().getSelectedItem());
        new ManageAccountController(user.getUsername()).display();
        updateTable();
    }

    public void updateTable() {

    }
}
