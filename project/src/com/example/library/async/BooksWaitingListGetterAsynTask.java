package com.example.library.async;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.library.MainActivity;
import com.example.library.OneBookActivity;
import com.example.library.R;
import com.example.library.SubscriberFragmentActivity;
import com.example.library.fragment.SubscriberWaitingListFragment;
import com.example.library.logger.Logger;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.datasource.SubscriberDataSource;
import com.example.library.model.entity.Book;
import com.example.library.rest.BookClientREST;
import com.example.library.rest.SubscriberPreferencyClientREST;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

// AsyncTask<Map => paramètres, Integer => progrès, Map => résultat>

public class BooksWaitingListGetterAsynTask extends AsyncTask<Map<String, Object>, Integer, Map<String, Object>> {
    private final static String LOG_TAG = BooksWaitingListGetterAsynTask.class.getName();
    private Activity activity;
    private SubscriberWaitingListFragment subscriberWaitingListFragment;

    @Override
    protected Map<String, Object> doInBackground(Map<String, Object>... data) {
        try {
            activity = (Activity) data[0].get("activity");
            subscriberWaitingListFragment = (SubscriberWaitingListFragment) data[0].get("subscriberWaitingListFragment");
            BookClientREST bookClientREST = new BookClientREST();
            bookClientREST.setActivity(activity);
            return bookClientREST.getBooks((Map<String, Object>) data[0].get("params"));
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on treating waiting list books request : " + e.getMessage());
            Log.e(LOG_TAG, "An error", e);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        //        setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) { 
        subscriberWaitingListFragment.constructBooksList(result);
    } 

}