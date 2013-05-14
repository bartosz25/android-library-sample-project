package com.example.library.model.entity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import java.io.Serializable;

public class TabInfo implements Serializable {
    private String tag;
    private Class clss;
    // Bundle is one of parameters passed to public static Fragment instantiate (Context context, String fname, Bundle args)
    // http://developer.android.com/reference/android/app/Fragment.html#instantiate(android.content.Context, java.lang.String, android.os.Bundle)
    private Bundle args;
    private Fragment fragment;
    private Context context;
    private String restTag;

    public TabInfo() {
    }
    
    public TabInfo(String tag, Class clazz, Bundle args, Context context, String restTag) {
        this.tag = tag;
        this.clss = clazz;
        this.args = args;
        this.context = context;
        this.restTag = restTag;
    }

    public String getTag() {
        return tag;
    }

    public Bundle getArgs() {
        return args;
    }
    
    public Class getClss() {
        return clss;
    }
    
    public Fragment getFragment() {
        return fragment;
    }
    
    public Context getContext() {
        return context;
    }
    
    public String getRestTag() {
        return restTag;
    }

    public void setTag(String tag) {
        this.tag= tag;
    }

    public void setClss(Class clss) {
        this.clss = clss;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void getRestTag(String restTag) {
        this.restTag = restTag;
    }
}