package com.powersoft.itec2017.framework;

import com.powersoft.itec2017.Main;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import javax.swing.plaf.nimbus.State;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    public static void executePreparedUpdate(String sql, Object... params) {
        PreparedStatement statement = null;
        try {
            statement = Main.getConnection().prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof byte[])
                    statement.setBytes(i + 1, ((byte[]) params[i]));
                else
                    statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String sql) {
        Statement statement = null;
        try {
            statement = Main.getConnection().createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet executePreparedQuery(String sql, Object... params) {
        PreparedStatement statement = null;
        try {
            statement = Main.getConnection().prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet executeQuery(String sql) {
        try {
            Statement statement = Main.getConnection().createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
