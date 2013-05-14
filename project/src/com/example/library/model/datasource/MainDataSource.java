package com.example.library.model.datasource;

import com.example.library.model.DatabaseInitializer;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MainDataSource {

    protected static DatabaseInitializer databaseInitializer;
    private static SQLiteDatabase database;
    private static final String LOG_TAG = MainDataSource.class.getName();

    public MainDataSource(Context context) {
        databaseInitializer = new DatabaseInitializer(context);
    }

    public static synchronized SQLiteDatabase getDatabaseInstance() throws SQLException {
        if (database == null) {
            Log.d(LOG_TAG, "Opening new database ");
            database = databaseInitializer.getWritableDatabase();
        } else {
            Log.d(LOG_TAG, "Old instance is returned");
        }
        return database;
    }

    public static void close() {
        if (database != null) {
            Log.d(LOG_TAG, "Database is not null. Close it.");
            database.close();
            database = null;
        } else {
            Log.d(LOG_TAG, "Attempting to close a null database object.");
        }
    }

}