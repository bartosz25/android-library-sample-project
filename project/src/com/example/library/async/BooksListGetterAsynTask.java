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
import com.example.library.view.OneLetterView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class BooksListGetterAsynTask extends AsyncTask<Map<String, Object>, Integer, Map<String, Object>> {
    private final static String LOG_TAG = BooksListGetterAsynTask.class.getName();
    private OneLetterView oneLetterView;
    private Activity activity;

    @Override
    protected Map<String, Object> doInBackground(Map<String, Object>... data) {
        try {
            activity = (Activity) data[0].get("activity");
            oneLetterView = (OneLetterView) data[0].get("oneLetterView");
            BookClientREST bookClientREST = new BookClientREST();
            bookClientREST.setActivity(activity);
            return bookClientREST.getBooks((Map<String, Object>) data[0].get("params"));
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on getting books list : " + e.getMessage());
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
        oneLetterView.appendBooks(result);
    }

// @Override
// protected void onPreExecute() {
// Log.d(LOG_TAG, "Before launching of AsynTask"); 
// }
}