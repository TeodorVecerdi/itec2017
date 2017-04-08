package com.powersoft.itec2017.window.controller;

import com.powersoft.itec2017.Main;
import com.powersoft.itec2017.framework.DatabaseManager;
import com.powersoft.itec2017.framework.QueryManager;
import com.powersoft.itec2017.security.PasswordEncryptionService;
import com.powersoft.itec2017.security.VerifierService;
import com.powersoft.itec2017.skins.PasswordFieldMaskableSkin;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    public TextField firstNameInput;
    public TextField lastNameInput;
    public TextField usernameInput;
    public TextField emailInput;
    public PasswordField passwordInput;
    public PasswordField retypePasswordInput;
    public ComboBox<String> homeCityInput;
    public ToggleButton showPasswords;

    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public RegisterController() {
        init();
    }

    public void init() {

    }

    public void register(ActionEvent event) {

        String errorMessage = VerifierService.verifyRegister(firstNameInput.getText(), lastNameInput.getText(), usernameInput.getText(), emailInput.getText(), passwordInput.getText(), retypePasswordInput.getText(), homeCityInput);
        if (!errorMessage.equals("ok")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Register form completed incorectly");
            alert.setContentText(errorMessage);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initStyle(StageStyle.UTILITY);

            alert.showAndWait();
        } else {
            register();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful Register");
            alert.setHeaderText("You have successfully registered");
            alert.setContentText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initStyle(StageStyle.UTILITY);
            alert.showAndWait();
            Parent loginParent = null;
            try {
                loginParent = FXMLLoader.load(getClass().getResource("../fxml/LoginWindow.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            assert loginParent != null;
            Scene scene = new Scene(loginParent);
            scene.getStylesheets().addAll("com/teodor/vecerdi/movielist/css/style.css","styles/glyphs_blue.css");
            stage.setScene(scene);
        }
    }


    public void login(ActionEvent event) {
        try {
            Parent loginParent = FXMLLoader.load(getClass().getResource("../fxml/LoginWindow.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loginParent);
            scene.getStylesheets().addAll("styles/glyphs_blue.css");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        try {
            Date date = new Date();
            PasswordEncryptionService PES = new PasswordEncryptionService();
            byte[] salt = PES.generateSalt();
            byte[] password = PES.getEncryptedPassword(passwordInput.getText(), salt);
            DatabaseManager.executePreparedUpdate(QueryManager.ADD_NEW_ACCOUNT, usernameInput.getText(), salt, password, firstNameInput.getText(), lastNameInput.getText(), emailInput.getText(), homeCityInput.getSelectionModel().getSelectedItem(), sdf.format(date));

            String sql = "CREATE TABLE " + usernameInput.getText() + " (\n" +
                    "  `id` INT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                    "  `LocationName` VARCHAR(200) COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
                    "  `StartDate` CHAR(10) COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
                    "  `EndDate` CHAR(10) COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
                    "  `Price` INT(11) COLLATE utf8mb4_unicode_ci NOT NULL\n" +
                    ")";
            DatabaseManager.executePreparedUpdate(sql);


        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordInput.setSkin(new PasswordFieldMaskableSkin(passwordInput, showPasswords));
        retypePasswordInput.setSkin(new PasswordFieldMaskableSkin(retypePasswordInput, showPasswords));
        GlyphsDude.setIcon(showPasswords, FontAwesomeIcon.EYE, "2em");
        showPasswords.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                GlyphsDude.setIcon(showPasswords, FontAwesomeIcon.EYE_SLASH, "2em");
            else
                GlyphsDude.setIcon(showPasswords, FontAwesomeIcon.EYE, "2em");
        });
    }
}
