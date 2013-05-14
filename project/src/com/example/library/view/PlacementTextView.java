package com.example.library.view;

import android.content.Context; 
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class PlacementTextView extends TextView {

    private final static String LOG_TAG = PlacementTextView.class.getName();
    private String placement;
    private String method;
    private String textId;
    private String resString;

    public PlacementTextView(Context context) {
        super(context);
    }

    public PlacementTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlacementTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getPlacement() {
        return placement;
    }
    public String getMethod() {
        return method;
    }
    public String getTextId() {
        return textId;
    }
    public String getResString() {
        return resString;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public void setTextId(String textId) {
        this.textId = textId;
    }
    public void setResString(String resString) {
        this.resString = resString;
    }

    public String toString() {
        return "PlacementTextView [placement = "+getPlacement()+", resString  = "+getResString()+" method = "+getMethod()+", textId = "+getTextId()+"]";
    }

}