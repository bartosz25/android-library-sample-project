package com.example.library.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.library.CustomizeActivity;

public class DatabaseInitializer extends SQLiteOpenHelper {

    public static final String TABLE_CUSTOMIZE = "customization";
    public static final HashMap<String, String> SCHEMA_CUSTOMIZE = new HashMap<String, String>() {{
        put("id", "id_cu");
        put("place", "place_cu");
        put("text", "text_cu");
        put("method", "method_cu");
    }};
    public static final String TABLE_SUBSCRIBER = "subscriber";
    public static final HashMap<String, String> SCHEMA_SUBSCRIBER = new HashMap<String, String>() {{
        put("login", "login_su");
        put("password", "password_su");
//put("state", "state_su");
    }};
    public static final String TABLE_PREFERENCY = "preferency";
    public static final HashMap<String, String> SCHEMA_PREFERENCY = new HashMap<String, String>() {{
        put("code", "code_pr");
        put("value", "value_pr");
    }};
    public static final String TABLE_WAITING_LIST = "waiting_list";
    public static final HashMap<String, String> SCHEMA_WAITING_LIST = new HashMap<String, String>() {{
        put("id", "book_wl");
        put("added", "added_time_wl");
    }};

    private static final String LOG_TAG = DatabaseInitializer.class.getName();
    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 1;
    private static final ArrayList<String> QUERIES = new ArrayList<String>() {{
        add("CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMIZE + "("+
            SCHEMA_CUSTOMIZE.get("id") + " text primary key, " +
            SCHEMA_CUSTOMIZE.get("place") + " text not null, " +
            SCHEMA_CUSTOMIZE.get("text") + " text not null, " +  
            SCHEMA_CUSTOMIZE.get("method") + " text not null" 
            +")"
        );
        /*add("INSERT INTO " + TABLE_CUSTOMIZE + " (" + SCHEMA_CUSTOMIZE.get("id") + " , " +             SCHEMA_CUSTOMIZE.get("place") + ", " + 
            SCHEMA_CUSTOMIZE.get("text")	+ ", " + SCHEMA_CUSTOMIZE.get("method") + ") VALUES " + 
            "(\"customizeHeaderContainer\", \""+CustomizeActivity.PLACEMENT_HEADER+"\", \"customize_title\", \"constructTitle\"), " + 
            "(\"customizeWriterContainer\", \""+CustomizeActivity.PLACEMENT_LEAD+"\", \"customize_writer\", \"constructLead\")," + 
            "(\"customizeDescContainer\", \""+CustomizeActivity.PLACEMENT_CONTENT+"\", \"customize_desc\", \"constructContent\")," +
            "(\"customizeActionsContainer\", \""+CustomizeActivity.PLACEMENT_FOOTER+"\", \"customize_actions\", \"constructFooter\")"
        );*/
        add("CREATE TABLE IF NOT EXISTS " + TABLE_SUBSCRIBER + "("+
            SCHEMA_SUBSCRIBER.get("login") + " text not null primary key, " +
            SCHEMA_SUBSCRIBER.get("password") + " text not null  "
            +")"
        );
        add("CREATE TABLE IF NOT EXISTS " + TABLE_PREFERENCY + "("+
            SCHEMA_PREFERENCY.get("code") + " text not null primary key, " +
            SCHEMA_PREFERENCY.get("value") + " text not null"
            +")"
        );
        add("CREATE TABLE IF NOT EXISTS " + TABLE_WAITING_LIST + "("+
            SCHEMA_WAITING_LIST.get("id") + " integer not null primary key, " +
            SCHEMA_WAITING_LIST.get("added") + " integer not null"
            +")"
        );
    }};

    public DatabaseInitializer(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseInitializer(Context context, String databaseName, CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        for(String query : QUERIES) {
            Log.d(LOG_TAG, "Creating database with query " + query);
            try {
                database.execSQL(query);
            } catch (Exception e) {
                Log.d(LOG_TAG, "An error occured on executing query : " + query + ". Error was : " + e.getMessage());	
            }
        }
        ContentValues contentValuesHeader = new ContentValues();
        contentValuesHeader.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("id"), "customizeHeaderContainer");
        contentValuesHeader.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("place"), CustomizeActivity.PLACEMENT_HEADER);
        contentValuesHeader.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("text"), "customize_title");
        contentValuesHeader.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("method"), "constructTitle");
        Log.d(LOG_TAG, "Saving new customization values " + contentValuesHeader);
        database.insert(DatabaseInitializer.TABLE_CUSTOMIZE, null, contentValuesHeader);
        ContentValues contentValuesLead = new ContentValues();
        contentValuesLead.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("id"), "customizeWriterContainer");
        contentValuesLead.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("place"), CustomizeActivity.PLACEMENT_LEAD);
        contentValuesLead.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("text"), "customize_writer");
        contentValuesLead.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("method"), "constructLead");
        Log.d(LOG_TAG, "Saving new customization values " + contentValuesLead);
        database.insert(DatabaseInitializer.TABLE_CUSTOMIZE, null, contentValuesLead);
        ContentValues contentValuesContent = new ContentValues();
        contentValuesContent.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("id"), "customizeDescContainer");
        contentValuesContent.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("place"), CustomizeActivity.PLACEMENT_CONTENT);
        contentValuesContent.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("text"), "customize_desc");
        contentValuesContent.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("method"), "constructContent");
        Log.d(LOG_TAG, "Saving new customization values " + contentValuesContent);
        database.insert(DatabaseInitializer.TABLE_CUSTOMIZE, null, contentValuesContent);
        ContentValues contentValuesFooter = new ContentValues();
        contentValuesFooter.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("id"), "customizeActionsContainer");
        contentValuesFooter.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("place"), CustomizeActivity.PLACEMENT_FOOTER);
        contentValuesFooter.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("text"), "customize_actions");
        contentValuesFooter.put(DatabaseInitializer.SCHEMA_CUSTOMIZE.get("method"), "constructFooter");
        Log.d(LOG_TAG, "Saving new customization values " + contentValuesFooter);
        database.insert(DatabaseInitializer.TABLE_CUSTOMIZE, null, contentValuesFooter);
 
    /*add("INSERT INTO " + TABLE_CUSTOMIZE + " (" + SCHEMA_CUSTOMIZE.get("id") + " , " +             SCHEMA_CUSTOMIZE.get("place") + ", " + 
        SCHEMA_CUSTOMIZE.get("text")	+ ", " + SCHEMA_CUSTOMIZE.get("method") + ") VALUES " + 
        "(\"customizeHeaderContainer\", \""+CustomizeActivity.PLACEMENT_HEADER+"\", \"customize_title\", \"constructTitle\"), " + 
        "(\"customizeWriterContainer\", \""+CustomizeActivity.PLACEMENT_LEAD+"\", \"customize_writer\", \"constructLead\")," + 
        "(\"customizeDescContainer\", \""+CustomizeActivity.PLACEMENT_CONTENT+"\", \"customize_desc\", \"constructContent\")," +
        "(\"customizeActionsContainer\", \""+CustomizeActivity.PLACEMENT_FOOTER+"\", \"customize_actions\", \"constructFooter\")"
    );*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int fromVersion, int toVersion) {
        // actually it does nothing but you can put here some of updating SQL requests and executes its dependently on installed version
    }

}