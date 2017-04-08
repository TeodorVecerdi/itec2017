package com.powersoft.itec2017.framework;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by DragosTrett on 08.04.2017.
 */
public class ReportTravel {

    public static String returnMap(String homeLocation, String... orase) {
        String center = "Europe";
        int zoom = 3, w = 1340,  h = 400;
        String color = null;
        switch (homeLocation){
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
            default:{
                color = "black";
                break;
            }
        }
        Random random = new Random();
        String result = "https://maps.googleapis.com/maps/api/staticmap?center=" + center + "&zoom=" + zoom + "&size=" + w + "x" + h + "&maptype=roadmap";
        for (String oras : orase) {
            oras = oras.replace(' ', '+');
            result += "&markers=color:" + color + "%7Clabel:%7C" + oras;
        }
        result += "&key=AIzaSyAyQVIR0G0-GZLHVOBvqs7wj7O-YSgM7Mg";
        return result;
    }
    public static void exelReport( String username){
        String homeLocation="";
        try {
            ResultSet resultSet = DatabaseManager.executeQuery("SELECT City FROM accounts WHERE username = '" + username + "'");
            resultSet.next();
            homeLocation = resultSet.getString("City");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            FileChooser exportChooser = new FileChooser();
            exportChooser.setTitle("Select directory");
            File File = exportChooser.showSaveDialog(null);
            String exportFile = File.toString()+ ".xls";

            FileOutputStream fileOut = null;
            fileOut = new FileOutputStream(exportFile);

            Workbook wb = new HSSFWorkbook();

            String selectDataToExport = "SELECT * FROM " + username;
            ResultSet resultSet = DatabaseManager.executePreparedQuery(selectDataToExport);
            Map<String, Object[]> data = new HashMap<String, Object[]>();
            //data.put("1", new Object[] {username});
            while (resultSet.next()) {
                data.put(resultSet.getString("id"), new Object[] {resultSet.getString("LocationNAme"), resultSet.getString("StartDate"), resultSet.getString("EndDate"), resultSet.getString("Price")});
                //writer.println(resultSet.getString("LocationName")+";"+resultSet.getString("StartDate")+";"+resultSet.getString("EndDate") + ";" + resultSet.getInt("Price"));
            }
            Sheet sheet1 = wb.createSheet("Data");
            Set<String> keyset = data.keySet();
            int rownum = 0;
            for (String key : keyset) {
                Row row = sheet1.createRow(rownum++);
                Object [] objArr = data.get(key);
                int cellnum = 0;
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellnum++);
                    if(obj instanceof Date)
                        cell.setCellValue((Date)obj);
                    else if(obj instanceof Boolean)
                        cell.setCellValue((Boolean)obj);
                    else if(obj instanceof String)
                        cell.setCellValue((String)obj);
                    else if(obj instanceof Double)
                        cell.setCellValue((Double)obj);
                }
            }
            InputStream is = null;


            try {

                String selectLocations = "SELECT * FROM " + username;
                    ResultSet resultSet2 = DatabaseManager.executeQuery(selectLocations);
                    List<String> orase = new ArrayList<>();
                    while(resultSet2.next()){
                        orase.add(resultSet2.getString("LocationName"));
                    }

                is = new URL(returnMap(homeLocation,  orase.toArray(new String[orase.size()]))).openStream();
                byte[] bytes = IOUtils.toByteArray(is);

                is.close();
                int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                CreationHelper helper = wb.getCreationHelper();

                //create sheet
                Sheet sheet = wb.createSheet("Image");

                // Create the drawing patriarch.  This is the top level container for all shapes.
                Drawing drawing = sheet.createDrawingPatriarch();

                //add a picture shape
                ClientAnchor anchor = helper.createClientAnchor();
                //set top-left corner of the picture,
                //subsequent call of Picture#resize() will operate relative to it
                anchor.setCol1(3);
                anchor.setRow1(2);
                Picture pict = drawing.createPicture(anchor, pictureIdx);

                //auto-size picture relative to its top-left corner
                pict.resize();

                //save workbook

                if(wb instanceof HSSFWorkbook) exportFile += "x";

                try {
                    wb.write(fileOut);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | SQLException e) {
            System.out.print("shit");
        }
    }
}
