package com.powersoft.itec2017.window.controller;

import com.powersoft.itec2017.framework.DatabaseManager;
import com.powersoft.itec2017.security.PasswordEncryptionService;
import com.powersoft.itec2017.security.PasswordVerifierService;
import com.powersoft.itec2017.security.UsernameVerifierService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Pattern;

public class ManageAccountController {
    public TextField fieldTextField;
    public ComboBox fieldComboBox;
    Stage stage = new Stage();
    private String username;

    public ManageAccountController(String username) {
        this.username = username;
    }

    public void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ManageAccountWindow.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("Duplicates")
    public void changeButton(ActionEvent event) {
        String sql = "UPDATE accounts SET ? = ? WHERE Username = " + "'" + username + "'";
        String selectedItem = (String) fieldComboBox.getSelectionModel().getSelectedItem();
        if (selectedItem.isEmpty())
            new Alert(Alert.AlertType.ERROR, "SELECT AN ITEM", ButtonType.CLOSE);
        else if (fieldTextField.getText().isEmpty())
            new Alert(Alert.AlertType.ERROR, "SELECT AN INPUT SHOULD NOT BE EMPTY", ButtonType.CLOSE);
        else if (selectedItem.equals("Password")) {
            String passwordResponse = PasswordVerifierService.verify(fieldTextField.getText());
            if (Objects.equals(passwordResponse, "ok")) {
                try {
                    ResultSet resultSet = DatabaseManager.executeQuery("SELECT Salt FROM accounts WHERE Username = '" + username +"'");
                    resultSet.next();
                    PasswordEncryptionService PES = new PasswordEncryptionService();
                    sql = sql.replaceFirst(Pattern.quote("?"), "Password");
                    DatabaseManager.executePreparedUpdate(sql, PES.getEncryptedPassword(fieldTextField.getText(), resultSet.getBytes("Salt")));
                    System.out.println(sql);
                } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, null, ButtonType.CLOSE).setHeaderText(passwordResponse);
            }
        }else {
            String sql2 = "UPDATE accounts SET " + selectedItem + " = ? WHERE Username = " + "'" + username + "'";
            DatabaseManager.executePreparedUpdate(sql2, fieldTextField.getText());
        }
    }

    public void exitButton(ActionEvent event) {
        stage.close();
    }
}
