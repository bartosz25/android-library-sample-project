package com.example.library.model.entity;

import java.io.Serializable;

public class Subscriber implements Serializable {

    //public static final int STATE_ERROR = 0;
    //public static final int STATE_OK = 1;

    private String login;
    private String password;
    //private int state;
    
    public Subscriber() {
        
    }
    
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    /*public int getState() { return state; }*/
    public String getAccessKey() {
        return login + password;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    /*public void setState(int state) { this.state = state; }
    public boolean areValidCredentials() { return state == STATE_OK;}*/

    public String toString() {
        return "Subscriber [login = "+getLogin()+", password = "+getPassword()+"]";
    }

}