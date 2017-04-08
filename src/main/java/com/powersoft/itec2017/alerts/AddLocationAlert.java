package com.powersoft.itec2017.alerts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddLocationAlert {

    private static String result;

    public static String display() {

        Stage window = new Stage();
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Location");
        window.setMinWidth(250);

        Label nameLocation = new Label();
        nameLocation.setText("Location Name:");

        TextField location = new TextField();
        TextField startDate = new TextField();
        TextField endDate = new TextField();
        startDate.setPromptText("dd/MM/yyyy");
        endDate.setPromptText("dd/MM/yyyy");
        TextField price = new TextField();
        price.setPromptText("$");
        Button yesButton = new Button("Yes");
        yesButton.setDefaultButton(true);
        Button noButton = new Button("No");
        // TODO: 07.04.2017 verificare format data
        yesButton.setOnAction(e -> {
            String error = "";
            if (!startDate.getText().matches("^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$"))
                error += "Start date format wrong\n";
            if (!endDate.getText().matches("^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$"))
                error += "End date format wrong\n";
            if (!error.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(error);
                alert.setContentText(null);
                alert.showAndWait();
            } else {
                result = String.valueOf(location.getText() + ";" + startDate.getText() + ";" + endDate.getText() + ";" + price.getText());
                window.close();
            }
        });

        noButton.setOnAction(e -> {
            result = "";
            window.close();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        HBox laba = new HBox();
        laba.setSpacing(10);
        laba.setAlignment(Pos.CENTER);
        laba.getChildren().addAll(yesButton, noButton);
        layout.getChildren().addAll(location, startDate, endDate, price, laba);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return result;
    }
}
