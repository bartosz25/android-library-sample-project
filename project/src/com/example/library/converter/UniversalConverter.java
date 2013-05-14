package com.example.library.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.dom4j.Node;

import com.example.library.logger.Logger;

import android.util.Log;

public class UniversalConverter {

    private final static String LOG_TAG  = UniversalConverter.class.getName();

    public static long fromStringToLong(String text) {
        long id = 0l; 
        try {
            id = Long.parseLong(text.trim());
        } catch(Exception e) {
            Log.e(LOG_TAG, "An error occured on converting " + text + " to long", e);
        }
        return id;
    }

    public static int fromStringToInt(String text) {
        int id = 0;
        try {
            id = Integer.parseInt(text.trim());
        } catch (Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on converting Strign to long ("+text+" : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on converting " + text + " to long", e);
        }
        return id;
    }

    public static int fromStringToHexInt(String text) {
        return fromStringToInt(Integer.toHexString(fromStringToInt(text)));
    }

    public static Date fromStringToDate(String text, String format) {
        Date date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            date = df.parse(text);
        }
        catch(ParseException e) {
            Logger.addMessage(LOG_TAG, "An error occured on converting String to Date ("+text+") : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on converting " + text + " to date", e);
        }
        return date;
    }

    public static String fromDateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static Date fromDateTimeToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
        calendar.set(Calendar.MILLISECOND, 0);  
        return calendar.getTime();
    }

    public static long fromDateTimeToLong(Date date) {
        Date dateConverted = UniversalConverter.fromDateTimeToDate(date);
        return dateConverted.getTime();
    }

}