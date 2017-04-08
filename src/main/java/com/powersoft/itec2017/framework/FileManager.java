package com.powersoft.itec2017.framework;

import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import jdk.nashorn.internal.parser.DateParser;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static void exportData(String username) {

        try {
            FileChooser exportChooser = new FileChooser();
            exportChooser.setTitle("Select directory");
            File exportFile = exportChooser.showSaveDialog(null);
            PrintWriter writer = new PrintWriter(exportFile, "UTF-8");
            writer.println(username);
            String selectDataToExport = "SELECT * FROM " + username;
            ResultSet resultSet = DatabaseManager.executePreparedQuery(selectDataToExport);
            while (resultSet.next()) {
                writer.println(resultSet.getString("LocationName")+";"+resultSet.getString("StartDate")+";"+resultSet.getString("EndDate") + ";" + resultSet.getInt("Price"));
            }
            writer.close();
        } catch (IOException | SQLException e) {
            System.out.print("shit");
        }
    }

    public static void importData() {
        BufferedReader br = null;
        FileReader fr = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file to import");
        File file = fileChooser.showOpenDialog(null);
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            String username = br.readLine();
            while ((sCurrentLine = br.readLine()) != null) {
                String [] data = sCurrentLine.split(";");
                String findExistant = "SELECT * FROM " + username + " WHERE LocationName = '" + data[0] + "' AND StartDate = '" + data[1] + "' AND EndDate = '" + data[2] + "' AND Price = '" + data[3] + "'";
                if(!DatabaseManager.executeQuery(findExistant).isBeforeFirst()){
                    String sqlInsertIfNotExists = "INSERT INTO " + username + "(LocationName, StartDate, EndDate, Price) VALUES(?,?,?,?)";
                    DatabaseManager.executePreparedUpdate(sqlInsertIfNotExists, data);
                }
            }


        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
