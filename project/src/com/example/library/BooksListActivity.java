package com.example.library;
 
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class BooksListActivity extends FragmentActivity  {

    private final static String LOG_TAG = BooksListActivity.class.getName();
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    Log.d(LOG_TAG, "Creating BooksListActivity");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_books_list);

    Resources resources = getResources();
    TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
    tabs.setup();
    
    TabSpec tabAll = tabs.newTabSpec(resources.getString(R.string.books_tab_all));

    tabAll.setIndicator(resources.getString(R.string.books_tab_all));
    tabAll.setContent(new Intent(this, ContactFormActivity.class));
    tabs.addTab(tabAll);
    }
}