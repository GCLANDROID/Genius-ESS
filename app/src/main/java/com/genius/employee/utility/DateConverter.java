package com.genius.employee.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    public static String convert_Date_yyyy_MM_dd_To_dd_MMM_yyyy(String inputDateString){
        String outputTimeString="";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date = inputFormat.parse(inputDateString);
            String outputDate = outputFormat.format(date);
            outputTimeString = outputDate;
            //System.out.println(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputTimeString;
    }

    //yyyy-MM-dd

    public static String convert_Date_dd_MMM_yyyy_To_yyyy_MM_dd(String inputDateString){
        String outputTimeString="";
        DateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        try {
            Date date = inputFormat.parse(inputDateString);
            String outputDate = outputFormat.format(date);
            outputTimeString = outputDate;
            //System.out.println(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputTimeString;
    }

    public static String convert_Date_MM_DD_YYYY_To_yyyy_MM_dd(String inputDateString){
        String outputTimeString="";
        DateFormat inputFormat = new SimpleDateFormat("MM dd yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        try {
            Date date = inputFormat.parse(inputDateString);
            String outputDate = outputFormat.format(date);
            outputTimeString = outputDate;
            //System.out.println(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputTimeString;
    }
}
