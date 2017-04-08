package com.powersoft.itec2017.framework;

import java.util.Date;

public class Period {

    public static boolean isWithinPeriod(String startDate, String endDate, String startDateP, String endDateP) {

        Date conditionStart = getDateFromString(startDate), periodStart = getDateFromString(startDateP);
        Date conditionEnd = getDateFromString(endDate), periodEnd = getDateFromString(endDateP);

        return periodStart.after(conditionStart) && periodEnd.before(conditionEnd);
    }
    public static Date getDateFromString(String text){
        Date date = new Date();
        String [] string = text.split("/");
        date.setDate(Integer.parseInt(string[0]));
        date.setMonth(Integer.parseInt(string[1]));
        date.setYear(Integer.parseInt(string[2]));
        return date;
    }
}
