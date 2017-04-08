package com.powersoft.itec2017.security;

public class UsernameVerifierService {
    public static String verify(String s) {
        boolean ok = true;
        String response = "Username MUST:\n";
        String regex[] = {
                ".*[\\W].*",
                "^[a-zA-Z].*$",
                "(\\S+$)",
                ".{8,}$"
        };
        if (s.matches(regex[0])) {
            response += "Not contain any special characters\n";
            ok = false;
        }
        if (!s.matches(regex[1])) {
            response += "Start with a letter\n";
            ok = false;
        }
        if (!s.matches(regex[2])) {
            response += "Not contain any whitespace\n";
            ok = false;
        }
        if (!s.matches(regex[3])) {
            response += "Be at least eight characters long\n";
            ok = false;
        }
        if (ok) response = "ok";
        return response;
    }
}
