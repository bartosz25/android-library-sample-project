package com.example.library.model.entity;

import java.io.Serializable;

public class Customization implements Serializable {
    
    private String id;
    /**
     * Item's place (header, footer, lead, desc), used in dynamic build of book box layout.
     */
    private String place;
    /**
     * Item's text. This field is used by CustomizeActivity when we load the default text to a layout placement.
     */
    private String text;
    /**
     * BookHelper method called to consturct the content of one layout part.
     */
    private String method;
    
    public Customization() {
        
    }
    
    public String getId() {
        return id;
    }
    public String getPlace() {
        return place;
    }
    public String getText() {
        return text;
    }
    public String getMethod() {
        return method;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setPlace(String place) { 
        this.place = place;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    public String toString() {
        return "Customization [id = "+getId()+", place = "+getPlace()+", text = "+getText()+", method ="+getMethod()+"]";
    }

}