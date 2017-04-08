package com.powersoft.itec2017.window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Window extends Application{

    private Stage window;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("fxml/LoginWindow.fxml"));
        window.setTitle("Log In");
//        window.setFullScreen(true);


        scene = new Scene(root);
        scene.getStylesheets().addAll("styles/glyphs_blue.css");
        window.setScene(scene);
        window.show();
    }

    public Stage getWindow() {
        return window;
    }

    public void show() {
        launch();
    }


}
