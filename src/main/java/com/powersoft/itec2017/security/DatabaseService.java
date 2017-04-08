package com.powersoft.itec2017.security;

import com.powersoft.itec2017.Main;
import com.powersoft.itec2017.framework.DatabaseManager;
import com.powersoft.itec2017.framework.QueryManager;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseService {

    public static boolean registerVerification(String username) {
        PreparedStatement statement;
        try {
            statement = Main.getConnection().prepareStatement(QueryManager.USERNAME_ALREADY_EXISTS);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return !resultSet.isBeforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginVerification(String username, String password) {
        PasswordEncryptionService PES = new PasswordEncryptionService();
        PreparedStatement statement;
        try {
            ResultSet resultSet = DatabaseManager.executePreparedQuery(QueryManager.LOGIN_VERIFICATION, username);
            if (!resultSet.isBeforeFirst())
                return false;
            resultSet.next();
            String dbUsername = resultSet.getString(1);
            byte[] dbSalt = resultSet.getBytes(2);
            byte[] dbPassword = resultSet.getBytes(3);

            return username.equals(dbUsername) && PES.authenticate(password, dbPassword, dbSalt);
        } catch (SQLException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
}
