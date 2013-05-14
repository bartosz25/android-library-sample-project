package com.example.library.adapter;

import android.app.ActionBar.LayoutParams;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactAutocompleteAdapter extends CursorAdapter implements Filterable {

    private final static String LOG_TAG = ContactAutocompleteAdapter.class.getName();
    private TextView numberField;
    private ContentResolver contentResolver;

    public ContactAutocompleteAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d(LOG_TAG, "Contact autocomplete adapter : bindView");
        int numberId = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
        String numberString = cursor.getString(numberId);
        int labelCol = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        String labelString = cursor.getString(labelCol);
        // notice views have already been inflated and layout has already been set so all you need to do is set the data
        LinearLayout horizontal = (LinearLayout) ((LinearLayout) view).getChildAt(0);
        if(horizontal == null) Log.d(LOG_TAG, "horizontal is null");
        else Log.d(LOG_TAG, "Horizontal is "+horizontal);
        Log.d(LOG_TAG, "horizontal child "+ horizontal.getChildAt(0));
        ((TextView) horizontal.getChildAt(0)).setText(numberString + " "  + labelString);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Log.d(LOG_TAG, "Contact autocomplete adapter : newView");
        final LinearLayout ret = new LinearLayout(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        numberField = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, viewGroup, false);
        ret.setOrientation(LinearLayout.VERTICAL);

        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);

        // you can even add images to each entry of your autocomplete fields
        // this example does it programmatically using JAVA, but the XML analog is very similar
        int numberId = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
        String numberString = cursor.getString(numberId);
        int labelCol = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        String labelString = cursor.getString(labelCol);
        numberField.setText(numberString + " " + labelString);
        numberField.setTextSize(16);
        numberField.setTextColor(Color.GRAY);
        Log.d(LOG_TAG, "Adding new number "+numberString + " " + labelString);

        // an example of how you can arrange your layouts programmatically
        // place the number and icon next to each other
        horizontal.addView(numberField, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ret.addView(horizontal, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        return ret;
    }

    /**
     * Affiche le numéro de téléphone après l'avoir sélectionné.
     */
    @Override
    public String convertToString(Cursor cursor) {
        int numberCol = cursor.getColumnIndexOrThrow(CommonDataKinds.Phone.NUMBER);
        return cursor.getString(numberCol);
    }

    /**
     * Cette méthode sert à rajouter une contrainte dans ContentResolver et de sélectionner les numéros correspondants au match
     */
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (getFilterQueryProvider() != null) return getFilterQueryProvider().runQuery(constraint);
        StringBuilder buffer = null;
        String[] args = null;
        if (constraint != null) {
            buffer = new StringBuilder();
            buffer.append("("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " IS NOT NULL AND " +  ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL ) AND ");
            buffer.append("(");
            buffer.append("UPPER(");
            buffer.append(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            buffer.append(") GLOB ? OR ");
            buffer.append(ContactsContract.CommonDataKinds.Phone.NUMBER);
            //buffer.append(PhoneLookup.NUMBER);
            buffer.append(" GLOB ?)");
            args = new String[] { constraint.toString().toUpperCase() + "*", constraint.toString() + "*" };
        }
        Log.d(LOG_TAG, "Querying with " + buffer.toString());
        Log.d(LOG_TAG, "Looking for " + ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        return contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, buffer == null ? null : buffer.toString(), args, CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
    }

}