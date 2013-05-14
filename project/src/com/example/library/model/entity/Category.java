package com.example.library.model.entity;

import java.io.Serializable;
import java.util.Map;

public class Category implements Serializable {

    private long id;
    private String name;
    private Map<String, String> urls;
    
    public Category() {
        
    }
    
    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Map<String, String> getUrls() {
        return urls;
    }

    public String toString() {
        return "Category [id = "+getId()+", name = "+getName()+", urls = "+getUrls()+"]";
    }

}