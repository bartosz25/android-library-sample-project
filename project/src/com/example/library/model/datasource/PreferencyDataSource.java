package com.example.library.model.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.library.logger.Logger;
import com.example.library.model.DatabaseInitializer;
import com.example.library.model.entity.Customization;
import com.example.library.model.entity.Subscriber;
import com.example.library.view.PlacementTextView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
 
public class PreferencyDataSource extends MainDataSource {

    private static final String LOG_TAG = SubscriberDataSource.class.getName();

    public PreferencyDataSource(Context context) {
        super(context);
    }

    public Map<String, String> getPreferencies() {
        Map<String, String> preferencies = new HashMap<String, String>();
        Cursor cursor = getDatabaseInstance().query(DatabaseInitializer.TABLE_PREFERENCY, new String[] {DatabaseInitializer.SCHEMA_PREFERENCY.get("code"),  
        DatabaseInitializer.SCHEMA_PREFERENCY.get("value")},  null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                preferencies.put(cursor.getString(0), cursor.getString(1));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return preferencies;
    }

    public void savePreferencies(Map<String, String> preferencies) {
        getDatabaseInstance().beginTransaction();
        try {
            Log.d(LOG_TAG, "Saving preferencies " + preferencies);
            getDatabaseInstance().delete(DatabaseInitializer.TABLE_PREFERENCY, "", new String[]{}); 
            if (preferencies != null) {
                for (Map.Entry<String, String> preferency : preferencies.entrySet()) { 
                    ContentValues prefContent = new ContentValues();
                    prefContent.put(DatabaseInitializer.SCHEMA_PREFERENCY.get("code"), preferency.getKey());
                    prefContent.put(DatabaseInitializer.SCHEMA_PREFERENCY.get("value"), preferency.getValue());
                    Log.d(LOG_TAG, "Saving new customization values " + prefContent);
                    getDatabaseInstance().insert(DatabaseInitializer.TABLE_PREFERENCY, null, prefContent); 
                }
            }
            getDatabaseInstance().setTransactionSuccessful();
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on saving preferencies : " + e.getMessage());
            Log.e(LOG_TAG, "An exception occurred on saving new places", e);
        } finally {
            getDatabaseInstance().endTransaction();
        }
    }

}