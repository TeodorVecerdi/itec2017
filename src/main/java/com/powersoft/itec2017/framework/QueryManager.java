package com.powersoft.itec2017.framework;

public class QueryManager {
    public static final String ADD_NEW_ACCOUNT = "INSERT INTO accounts(Username, Salt, Password, FirstName, LastName, Email, City, StartDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final java.lang.String LOGIN_VERIFICATION = "SELECT username, salt, password FROM accounts WHERE username = ?";
    public static final java.lang.String USERNAME_ALREADY_EXISTS = "";
}
