package com.example.library.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.library.view.OneBookView;
import com.example.library.view.OneLetterView;
import com.example.library.context.MainApplication;
import com.example.library.model.entity.Letter;
public class LetterAdapter extends ArrayAdapter<Letter> { //BaseAdapter {

    private final static String LOG_TAG = LetterAdapter.class.getName();
    private Context context;
    private List<Letter> elements;
    private Activity activity;
    private Map<String, OneLetterView> cache = new HashMap<String, OneLetterView>();

    public LetterAdapter(Context context, int resource, List<Letter> elements, Activity activity) {
        super(context, resource, elements);
        this.context = context;
        this.elements = elements;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        if(elements == null) return 0;
        return elements.size();
    }

    @Override
    public Letter getItem(int i) {
        return elements.get(i);
    }

    @Override
    public long getItemId(int i) { 
        if(elements.get(i) != null) return elements.get(i).getId();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OneLetterView oneLetterView;
        // sans comparaison des tags, on arrivait à la situation où une lettre s'affichait 2 ou 3 fois dans le même écran
        if(convertView != null && cache.containsKey(elements.get(position).getCode())) {
        //if(convertView != null && ((OneLetterView)convertView).getTag().equals(elements.get(position).getCode())) {
            oneLetterView = cache.get(elements.get(position).getCode()); //(OneLetterView) convertView;
            Log.d(LOG_TAG, "Got from cache => " + elements.get(position).getCode());
        } else {
            if(convertView != null) Log.d(LOG_TAG, "Creating new OneLetterView despite of convertView object " + ((OneLetterView)convertView).getTag() + " . Called letter is " + elements.get(position));
            oneLetterView = new OneLetterView(context, elements.get(position), activity);    
            oneLetterView.setTag(elements.get(position).getCode());
            cache.put(elements.get(position).getCode(), oneLetterView); 
        }
        Log.d(LOG_TAG, "Cached values are : "+cache);
        MainApplication app = (MainApplication) context.getApplicationContext();
        int viewVisibility = View.INVISIBLE;
        if(app.canShowLetter(elements.get(position).getCode())) viewVisibility = View.VISIBLE;
        oneLetterView.setVisibility(viewVisibility);
        return oneLetterView;
    }

}