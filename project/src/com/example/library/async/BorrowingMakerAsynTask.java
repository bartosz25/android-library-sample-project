package com.example.library.async;
import com.example.library.BaseActivity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.rest.BorrowingClientREST;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

 
public class BorrowingMakerAsynTask extends AsyncTask<Map<String, Object>, Integer, Map<String, Object>> {
    private final static String LOG_TAG = BorrowingMakerAsynTask.class.getName();
    private String message;
    private BaseActivity activity;
    // Map... userData correspond au premier paramètre du AsyncTask<Map, Integer, Map>
    @Override
    protected Map<String, Object> doInBackground(Map<String, Object>... data) {
        activity = (BaseActivity) data[0].get("activity");
        BorrowingClientREST borrowingClientREST = new BorrowingClientREST();
        borrowingClientREST.setActivity(activity);
        return borrowingClientREST.borrow((Long) data[0].get("id"), (Date) data[0].get("date"));
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) {
        activity.handleBorrow(result);
    }

}