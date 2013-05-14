package com.example.library.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView; 
 
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Customization;
import com.example.library.model.entity.Letter;
import com.example.library.view.OneBorrowingView;

public class BorrowingAdapter extends ArrayAdapter<Book> {

    private final static String LOG_TAG = BorrowingAdapter.class.getName();
    private Context context;
    private List<Customization> customizations;
    private List<Book> books;

    public BorrowingAdapter(Context context, int resource, List<Book> books) {
        super(context, resource, books);
        this.context = context;
        this.books = books;
        Log.d(LOG_TAG, "Calling BookAdapter constructor with params : "+context + " and " + books);
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Book getItem(int i) {
        return books.get(i);
    }

    @Override
    public long getItemId(int i) {
        if(books.get(i) != null) return books.get(i).getId();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(LOG_TAG, "Getting view for book : " + books.get(position) + " and for context " + context);
        OneBorrowingView oneBorrowingView;
        try {
            if(convertView != null && ((OneBorrowingView)convertView).getTag().equals(""+books.get(position).getId())) {
                oneBorrowingView = (OneBorrowingView) convertView;
                Log.d(LOG_TAG, "After casting convertView to OneBookView");
            } else {
                oneBorrowingView = new OneBorrowingView(context, books.get(position), customizations);
                oneBorrowingView.setTag(""+books.get(position).getId());
                Log.d(LOG_TAG, "After making oneBookView at the first time");
            } 
            return oneBorrowingView;
        } catch(Exception e) {
            Log.d(LOG_TAG, "AAAAA", e);
        }
        return null;
    }

}