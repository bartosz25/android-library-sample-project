package com.example.library.model.entity;

import java.io.Serializable;
import java.util.Date;

public class Writer implements Serializable {

    private long id;
    private String firstName;
    private String famillyName;
    private Date bornDate;
    private Date deadDate;
    
    public Writer() {
        
    }
    
    public void setId(long id) {
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setFamillyName(String famillyName) {
        this.famillyName = famillyName;
    }
    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }
    public void setDeadDate(Date deadDate) {
        this.deadDate = deadDate;
    }

    public long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getFamillyName() {
        return famillyName;
    }
    public Date getBornDate() {
        return bornDate;
    }
    public Date getDeadDate() {
        return deadDate;
    }

    public String toString() {
        return "Writer [id = "+getId()+", firstName = "+getFirstName()+", famillyName = "+getFamillyName()+", bornDate = "+getBornDate()+", deadDate = "+getDeadDate()+"]";
    }

}