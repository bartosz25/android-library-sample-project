package com.example.library.model.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.library.logger.Logger;
import com.example.library.model.DatabaseInitializer;
import com.example.library.model.entity.Customization;
import com.example.library.view.PlacementTextView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class CustomizationDataSource extends MainDataSource {

    private static final String LOG_TAG = CustomizationDataSource.class.getName();

    public CustomizationDataSource(Context context) {
        super(context);
    }

    public List<Customization> getAllCustomizations() {
        List<Customization> customizations = new ArrayList<Customization>();  
        Cursor cursor = getDatabaseInstance().query(DatabaseInitializer.TABLE_CUSTOMIZE, new String[] {DatabaseInitializer.SCHEMA_CUSTOMIZE.get("id"),  
            DatabaseInitializer.SCHEMA_CUSTOMIZE.get("place"), DatabaseInitializer.SCHEMA_CUSTOMIZE.get("text"), DatabaseInitializer.SCHEMA_CUSTOMIZE.get("method")}, null, null, null, null, null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                customizations.add(fromCursorToObject(cursor));
                cursor.moveToNext();    
            }
            // Make sure to close the cursor
            cursor.close();
        }
        return customizations;
    }

    public boolean updatePlaces(Map<String, String> places, Activity activity) {
        boolean result = true;
        getDatabaseInstance().beginTransaction();
        try {
            for(Map.Entry<String, String> entry : places.entrySet()) {
                // get PlacementTextView
                int resId = activity.getResources().getIdentifier(""+entry.getValue(), "id", "com.example.library");
                PlacementTextView placementTextView = (PlacementTextView) activity.findViewById(resId);
                Log.d(LOG_TAG, "Found placementTextView " + placementTextView);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("place"), entry.getKey());
                contentValues.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("text"), placementTextView.getTextId());
                contentValues.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("method"), ""+placementTextView.getMethod());
                Log.d(LOG_TAG, "Saving new customization values " + contentValues + " for " + entry.getValue());
                getDatabaseInstance().update(DatabaseInitializer.TABLE_CUSTOMIZE, contentValues, DatabaseInitializer.SCHEMA_CUSTOMIZE.get("id") + " = ?", 
                new String[] {entry.getValue()});
            }
            getDatabaseInstance().setTransactionSuccessful();
        } catch (Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on saving customization data : " + e.getMessage());
            Log.e(LOG_TAG, "An exception occurred on saving new places", e);
            result = false;
        } finally {
            getDatabaseInstance().endTransaction();
        }
        return result;
    }

    private Customization fromCursorToObject(Cursor cursor) {
        Customization customization = new Customization();
        customization.setId(cursor.getString(0));
        customization.setPlace(cursor.getString(1));
        customization.setText(cursor.getString(2));
        customization.setMethod(cursor.getString(3));
        return customization;
    }

}