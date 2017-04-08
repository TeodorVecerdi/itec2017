package com.powersoft.itec2017.window.controller;

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
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField usernameInput;
    public PasswordField passwordInput;
    public ToggleButton showPassword;

    public void login(ActionEvent event) {
        String response = VerifierService.verifyLogin(usernameInput.getText(), passwordInput.getText());
        if (response.equals("ok")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/MainWindow.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                MainController controller = new MainController(usernameInput.getText());
                loader.setController(controller);
                Parent listView = loader.load();
                Scene scene = new Scene(listView);
                scene.getStylesheets().addAll("styles/glyphs_blue.css");
                stage.setTitle("Location Manager");
                stage.setScene(scene);
                stage.setFullScreen(true);

                System.out.println("LOGIN SUCCESSFUL");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR, null);
            error.setHeaderText(response);
            error.showAndWait();
        }
    }

    public void register(ActionEvent event) {
        try {
            Parent register = FXMLLoader.load(getClass().getResource("../fxml/RegisterWindow.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(register);
            scene.getStylesheets().addAll("com/teodor/vecerdi/movielist/css/style.css", "styles/glyphs_blue.css");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordInput.setSkin(new PasswordFieldMaskableSkin(passwordInput, showPassword));
        GlyphsDude.setIcon(showPassword, FontAwesomeIcon.EYE, "2em");
        showPassword.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                GlyphsDude.setIcon(showPassword, FontAwesomeIcon.EYE_SLASH, "2em");
            else
                GlyphsDude.setIcon(showPassword, FontAwesomeIcon.EYE, "2em");
        });

    }
}
