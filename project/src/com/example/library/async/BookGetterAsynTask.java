package com.example.library.async;

import java.util.HashMap;
import java.util.Map;

import com.example.library.MainActivity;
import com.example.library.OneBookActivity;
import com.example.library.R;
import com.example.library.SubscriberFragmentActivity;
import com.example.library.logger.Logger;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.datasource.SubscriberDataSource;
import com.example.library.model.entity.Book;
import com.example.library.rest.BookClientREST;
import com.example.library.rest.SubscriberPreferencyClientREST;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

// AsyncTask<Map => paramètres, Integer => progrès, Map => résultat>

public class BookGetterAsynTask extends
AsyncTask<Map<String, Object>, Integer, Map<String, Object>> {
    private final static String LOG_TAG = BookGetterAsynTask.class.getName();
    private OneBookActivity activity; 

    @Override
    protected Map<String, Object> doInBackground(Map<String, Object>... data) {
        try {
            activity = (OneBookActivity) data[0].get("activity");
            BookClientREST bookClientREST = new BookClientREST();
            bookClientREST.setActivity(activity);
            return bookClientREST.getBook((Long) data[0].get("id"));
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured treating book getter : " + e.getMessage());
            Log.e(LOG_TAG, " An erro occ", e);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) { 
        activity.fillUpBookData(result);
    }

// @Override
// protected void onPreExecute() {
// Log.d(LOG_TAG, "Before launching of AsynTask"); 
// }
}