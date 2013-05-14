package com.example.library.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.Node;

import com.example.library.logger.Logger;

import android.util.Log;

public class FromStringConverter {

    private final static String LOG_TAG  = FromStringConverter.class.getName();

    public static long fromStringToLong(String text) {
        long id = 0l; 
        try {
            id = Long.parseLong(text.trim());
        } catch (Exception e) {
            Log.e(LOG_TAG, "An error occured on converting " + text + " to long", e);
        }
        return id;
    }

    public static int fromStringToInt(String text) {
        int id = 0;
        try {
            id = Integer.parseInt(text.trim());
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on converting String ("+text+") to Int : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on converting " + text + " to long", e);
        }
        return id;
    }

    public static Date fromStringToDate(String text, String format) {
        Date date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            date = df.parse(text);
        }
        catch(ParseException e) {
            Logger.addMessage(LOG_TAG, "An error occured on converting text ("+text+") to date : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on converting " + text + " to date", e);
        }
        return date;
    }

}