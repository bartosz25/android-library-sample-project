package com.example.library.model.datasource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.library.logger.Logger;
import com.example.library.model.DatabaseInitializer;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Customization;
import com.example.library.model.entity.Subscriber;
import com.example.library.view.PlacementTextView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
 
public class WaitingListDataSource extends MainDataSource {

    private static final String LOG_TAG = WaitingListDataSource.class.getName();
    private static final int MAX_BOOKS = 10;

    public WaitingListDataSource(Context context) {
        super(context);
    }

    public List<Integer> getWaitingIds() {
        List<Integer> result = new ArrayList<Integer>();
        Cursor cursor = getDatabaseInstance().query(DatabaseInitializer.TABLE_WAITING_LIST, new String[] {DatabaseInitializer.SCHEMA_WAITING_LIST.get("id")}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                result.add(cursor.getInt(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return result;
    }

    public Map<String, Object> insertBooks(List<Book> books) {
        Map<String, Object> result = new HashMap<String, Object>();
        int newBooks = 0;
        boolean isOk = false;
        String messageStringKey = "book_added_error";
        String complement = "";
        StringBuffer alreadyAdded = new StringBuffer();
        getDatabaseInstance().beginTransaction();
        List<Integer> addedBooks = getWaitingIds();
        int actuallyAdded = addedBooks.size();
        long time = new Date().getTime();
        try {
            for (Book book : books) {
                if (!addedBooks.contains((int)book.getId()) && actuallyAdded < MAX_BOOKS) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseInitializer.SCHEMA_WAITING_LIST.get("id"), book.getId());
                    contentValues.put(DatabaseInitializer.SCHEMA_WAITING_LIST.get("added"), (int)time);
                    Log.d(LOG_TAG, "Saving new book at waiting list " + contentValues);
                    getDatabaseInstance().insert(DatabaseInitializer.TABLE_WAITING_LIST, null, contentValues);
                    newBooks++;
                    actuallyAdded++;
                } else if (addedBooks.contains((int)book.getId())) {
                    alreadyAdded.append(book.getTitle());
                    alreadyAdded.append(",");
                } else {
                    messageStringKey = "book_limit_error";
                    complement = ""+MAX_BOOKS;
                    break;
                }
            }
            getDatabaseInstance().setTransactionSuccessful();
            isOk = true;
            messageStringKey = "book_added_success";
            complement = ""+newBooks;
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured saving books to waiting list : " + e.getMessage());
            Log.e(LOG_TAG, "An exception occurred on saving new places", e);
            complement = "error_saving_db";
            newBooks = 0;
            isOk = false;
        } finally {
            getDatabaseInstance().endTransaction();
        }
        // change the last , to .
        int lastPointIndex = alreadyAdded.lastIndexOf(",");
        if(lastPointIndex > -1) {
            alreadyAdded.replace(lastPointIndex, (lastPointIndex+1), ".");
        }
        if(newBooks == 0 && isOk) {
            isOk = false;
            messageStringKey = "book_added_error";
        }

        result.put("alreadyAdded", alreadyAdded.toString());
        result.put("addedBooks", newBooks);
        result.put("result", isOk);
        result.put("message", messageStringKey);
        result.put("messageComplement", complement);
        return result;
    }

    public Map<String, Object> insertBook(Book book) {
        List<Book> books = new ArrayList<Book>();
        books.add(book);
        return insertBooks(books);
    }

    public boolean deleteItem(int id) {
        boolean result = false;
        getDatabaseInstance().beginTransaction();
        long time = new Date().getTime();
        try {
            Log.d(LOG_TAG, "Deleting item " + id);
            getDatabaseInstance().delete(DatabaseInitializer.TABLE_WAITING_LIST, DatabaseInitializer.SCHEMA_WAITING_LIST.get("id") + " = ?", new String[]{""+id});
            getDatabaseInstance().setTransactionSuccessful();
            result = true;
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on deleting an item : " + e.getMessage());
            Log.e(LOG_TAG, "An exception occurred on deleting an item", e);
        } finally {
        getDatabaseInstance().endTransaction();
        }	
        return result;
    }

}