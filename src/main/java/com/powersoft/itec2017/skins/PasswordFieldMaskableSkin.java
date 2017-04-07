package com.powersoft.itec2017.skins;

import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class PasswordFieldMaskableSkin extends TextFieldSkin {
    public static final char BULLET = '\u25CF';
    private ToggleButton toggleButton;
    boolean selected = false;

    public PasswordFieldMaskableSkin(TextField textField, ToggleButton toggleButton) {
        super(textField);
        this.toggleButton = toggleButton;
        toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            selected = newValue;
            textField.setText(textField.getText());
        });
    }


    protected String maskText(String txt) {
        if (getSkinnable() instanceof PasswordField && !selected) {
            int n = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n);
            for (int i = 0; i < n; i++)
                passwordBuilder.append(BULLET);

            return passwordBuilder.toString();
        } else
            return txt;
    }
}