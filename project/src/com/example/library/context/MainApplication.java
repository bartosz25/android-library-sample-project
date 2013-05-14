package com.example.library.context;

import java.util.Date;

import com.example.library.converter.UniversalConverter;

import android.app.Application;

// TODO : (plus tard) utiliser Joda Time pour les comparaisons http://joda-time.sourceforge.net/ ou http://stackoverflow.com/questions/3144387/compare-two-dates-in-java
public class MainApplication extends Application {

    private final static String LOG_TAG = MainApplication.class.getName();
    public static final String BASE_URL = "http://www.google.com";
    public static final String SENDER_MAIL = "bartkonieczny@yahoo.fr";
    private Date lastCheckedDate;
    private String displayedLetter;

    public boolean canDoBorrowingCheck() {
        return (lastCheckedDate == null || (UniversalConverter.fromDateTimeToDate(lastCheckedDate).before(UniversalConverter.fromDateTimeToDate(new Date()))));
    }

    public Date getLastCheckedDate() {
        return lastCheckedDate;
    }
    public String getDisplayedLetter() {
        return displayedLetter;
    }
    public void setLastCheckedDate(Date lastCheckedDate) {
        this.lastCheckedDate = lastCheckedDate;
    }
    public void setDisplayedLetter(String displayedLetter) {
        this.displayedLetter = displayedLetter;
    }
    // displayedLetter.length() = show all or something like that
    public boolean canShowLetter(String letter) {
        return (displayedLetter == null || (displayedLetter != null && displayedLetter.length() > 1) || letter.equals(displayedLetter));
    }

}