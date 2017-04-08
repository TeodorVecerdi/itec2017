package com.powersoft.itec2017.framework;

import com.powersoft.itec2017.Main;

import javax.swing.plaf.nimbus.State;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    public static void executePreparedUpdate(String sql, Object ... params) throws SQLException {
        PreparedStatement statement = Main.getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i+1, params[i]);
        }
        statement.executeUpdate();
    }
    public static void executeUpdate(String sql) throws SQLException {
        Statement statement = Main.getConnection().createStatement();
        statement.executeUpdate(sql);
    }
    public static ResultSet executePreparedQuery(String sql, Object ... params) throws SQLException {
        PreparedStatement statement = Main.getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i+1, params[i]);
        }
        return statement.executeQuery();
    }
    public static ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = Main.getConnection().createStatement();
        return statement.executeQuery(sql);
    }
}
