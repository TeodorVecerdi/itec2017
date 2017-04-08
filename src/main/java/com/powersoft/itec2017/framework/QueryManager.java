package com.powersoft.itec2017.framework;

public class QueryManager {
    public static final String ADD_NEW_ACCOUNT = "INSERT INTO accounts(Username, Salt, Password, FirstName, LastName, Email, City, StartDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String LOGIN_VERIFICATION = "SELECT username, salt, password FROM accounts WHERE username = ?";
    public static final String USERNAME_ALREADY_EXISTS = "SELECT Username FROM accounts WHERE Username = ?";
}
