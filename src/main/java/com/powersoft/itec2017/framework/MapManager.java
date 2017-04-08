package com.powersoft.itec2017.framework;

import javafx.event.ActionEvent;
import javafx.scene.web.WebEngine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapManager {
    public static String getMap(String startDate, String endDate, String username) {
        String selectLocations = "SELECT * FROM " + username;
        String getUserLocation = "SELECT City FROM accounts WHERE Username = '" + username + "'";
        boolean filter = false;
        if (!startDate.isEmpty() && !endDate.isEmpty())
            filter = true;
        try {
            ResultSet resultSet = DatabaseManager.executeQuery(selectLocations);
            ResultSet resultSet2 = DatabaseManager.executeQuery(getUserLocation);
            resultSet2.next();

            String homeLocation = resultSet2.getString("City");

            List<String> orase = new ArrayList<>();
            while (resultSet.next()) {
                boolean isOk = true;
                String compStartDate = resultSet.getString("StartDate");
                String compEndDate = resultSet.getString("EndDate");
                if (filter)
                    isOk = Period.isWithinPeriod(startDate, endDate, compStartDate, compEndDate);
                if (isOk) {
                    orase.add(resultSet.getString("LocationName"));
                }

            }
            String str = "<center><img src=\"" + loadMap(homeLocation, orase.toArray(new String[orase.size()])) + "\"/></center>";
            System.out.println(orase);
            System.out.println(str);
            return str;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String loadMap(String homeLocation, String... orase) {
        String center = "Europe";
        int zoom = 3, w = 590, h = 345;
        String color = null;
        switch (homeLocation) {
            case "Timisoara": {
                color = "red";
                break;
            }
            case "Bucuresti": {
                color = "blue";
                break;
            }
            case "Brasov": {
                color = "green";
                break;
            }
            case "Sibiu": {
                color = "pink";
                break;
            }
        }
        String result = "https://maps.googleapis.com/maps/api/staticmap?center=" + center + "&zoom=" + zoom + "&size=" + w + "x" + h + "&maptype=roadmap";
        for (String oras : orase) {
            oras = oras.replace(' ', '+');
            result += "&markers=color:" + color + "%7Clabel:%7C" + oras;
        }
        result += "&key=AIzaSyAyQVIR0G0-GZLHVOBvqs7wj7O-YSgM7Mg";
        return result;
    }
}
