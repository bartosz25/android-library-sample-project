package com.example.library.model.datasource;

import java.util.ArrayList;
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
 
public class SubscriberDataSource extends MainDataSource {

    private static final String LOG_TAG = SubscriberDataSource.class.getName();

    public SubscriberDataSource(Context context) {
        super(context);
    }

    public boolean insertNewConfiguration(Map<String, Object> config) {
        boolean result = true;
        getDatabaseInstance().beginTransaction();
        try {
            getDatabaseInstance().delete(DatabaseInitializer.TABLE_SUBSCRIBER, "", new String[]{});

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseInitializer.SCHEMA_SUBSCRIBER.get("login"), (String)config.get("login"));
            contentValues.put(DatabaseInitializer.SCHEMA_SUBSCRIBER.get("password"), (String)config.get("password"));
// contentValues.put(DatabaseInitializer.SCHEMA_SUBSCRIBER.get("state"), 3);
            Log.d(LOG_TAG, "Saving new customization values " + contentValues);
            getDatabaseInstance().insert(DatabaseInitializer.TABLE_SUBSCRIBER, null, contentValues);
            getDatabaseInstance().delete(DatabaseInitializer.TABLE_PREFERENCY, "", new String[]{});
            getDatabaseInstance().setTransactionSuccessful();
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on inserting new configuration : " + e.getMessage());
            Log.e(LOG_TAG, "An exception occurred on saving new places", e);
            result = false;
        } finally {
            getDatabaseInstance().endTransaction();
        }
        return result;
    }

    public Subscriber getSubscriber() {
        // there are only one row with subscriber data
        Cursor cursor = getDatabaseInstance().query(DatabaseInitializer.TABLE_SUBSCRIBER, new String[] {DatabaseInitializer.SCHEMA_SUBSCRIBER.get("login"), DatabaseInitializer.SCHEMA_SUBSCRIBER.get("password")}, null, null, null, null, null, "1");
        if(cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        Subscriber subscriber = fromCursorToObject(cursor);
        cursor.close();
        return subscriber;
    }

    private Subscriber fromCursorToObject(Cursor cursor) {
        if(cursor == null) return null;
        Subscriber subscriber = new Subscriber();
        //subscriber.setState(cursor.getInt(0));
        subscriber.setLogin(cursor.getString(0));
        subscriber.setPassword(cursor.getString(1));
        return subscriber;
    }

}