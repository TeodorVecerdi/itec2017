package com.powersoft.itec2017;

import com.powersoft.itec2017.framework.DatabaseConnector;
import com.powersoft.itec2017.framework.Period;
import com.powersoft.itec2017.window.Window;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static Connection connection;
    public static void main(String [] args) {

        try {
            connection = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new Window().show();
    }

    public static Connection getConnection() {
        return connection;
    }
}
