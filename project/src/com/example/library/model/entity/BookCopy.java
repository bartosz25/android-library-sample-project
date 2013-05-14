package com.example.library.model.entity;

import java.io.Serializable;

public class BookCopy implements Serializable {

    private String reference;
    private String condition;
    private String state;
    
    public BookCopy() {
        
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getReference() {
        return reference;
    }
    public String getCondition() {
        return condition;
    }
    public String getState() {
        return state;
    }

    public String toString() {
        return "BookCopy [reference = "+getReference()+", condition = "+getCondition()+", state = "+getState()+"]";
    }

}