package com.example.library.model.entity;

import java.io.Serializable;

import android.util.Log;

public class Letter implements Serializable {

    private static final String LOG_TAG = "Letter";
    private static final String ID_PREFIX = "100";

    private int id;
    private String code;
    private int elementsCounter;
    
    public Letter() {
        
    }
    
    public Letter(int id, String code, int elementsCounter) {
        setId(id);
        setCode(code);
        setElementsCounter(elementsCounter);
    }

    public int getId() {
        return id;
    }
    public String getCode() {
        return code;
    }
    public int getElementsCounter() {
        return elementsCounter;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setElementsCounter(int elementsCounter) {
        this.elementsCounter = elementsCounter;
    }

    public String makeStringId() {
        Log.d(LOG_TAG, "Using " + "100"+getId());
        return ID_PREFIX+getId();
    }

    public static String makeStaticStringId(int id) {
        return ID_PREFIX+id;
    }

    public String toString() {
        return "Letter [id = "+getId()+", code = "+getCode()+", elementsCounter = "+getElementsCounter()+"]";
    }

}