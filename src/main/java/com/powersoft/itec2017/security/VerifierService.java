package com.powersoft.itec2017.security;

import javafx.scene.control.ComboBox;

public class VerifierService {
    public static String verifyRegister(String firstName, String lastName, String username, String email, String password, String retypePassword, ComboBox city) {
        String response = "";
        boolean ok = true;

        if (firstName.isEmpty()) {
            response += "First Name Field must not be empty\n";
            ok = false;
        }
        if (lastName.isEmpty()) {
            response += "Last Name Field must not be empty\n";
            ok = false;
        }
		if (email.isEmpty()) {
            response += "Email Address Field must not be empty\n";
            ok = false;
        }
        if (username.isEmpty()) {
            response += "Username Field must not be empty\n";
            ok = false;
        }
        
        if (password.isEmpty()) {
            response += "Password Field must not be empty\n";
            ok = false;
        }
        if (retypePassword.isEmpty()) {
            response += "Retype Password Field must not be empty\n";
            ok = false;
        }
        if (city.getSelectionModel().isEmpty()) {
            response += "Select a home city\n";
            ok = false;
        }
        if (!password.equals(retypePassword)) {
            response += "Password must equal Retype Password\n";
            ok = false;
        }

        String passwordResponse = PasswordVerifierService.verify(password);
        if (!passwordResponse.equals("ok")) {
            response += passwordResponse;
            ok = false;
        }
        String usernameResponse = UsernameVerifierService.verify(username);
        if (!usernameResponse.equals("ok")) {
            response += usernameResponse;
            ok = false;
        }

        if (ok) response = "ok";
        return response;
    }

    public static String verifyLogin(String username, String password) {
        String response = "";
        boolean ok = true;

        if (username.isEmpty()) {
            response += "Username Field must not be empty\n";
            ok = false;
        }

        if (password.isEmpty()) {
            response += "Password Field must not be empty\n";
            ok = false;
        }

        if (ok && !DatabaseService.loginVerification(username, password)) {
            response = "\nUSERNAME OR PASSWORD ARE INCORRECT\n";
            ok = false;
        }

        if (ok) response = "ok";
        return response;
    }
}
