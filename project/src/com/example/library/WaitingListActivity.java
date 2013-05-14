package com.example.library;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import android.view.View;
import com.example.library.adapter.BookAdapter;
import com.example.library.model.entity.Book;

public class WaitingListActivity extends BaseActivity  {
	private final static String LOG_TAG = WaitingListActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Creating BooksListActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new BookAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<Book>()));

        /*gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Log.d(LOG_TAG, "CLicked on "+position);
                Toast.makeText(WaitingListActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });*/
        
        
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}