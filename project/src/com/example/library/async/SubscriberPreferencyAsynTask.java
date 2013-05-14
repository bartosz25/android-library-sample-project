package com.example.library.async;

import java.util.HashMap;
import java.util.Map;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.SubscriberFragmentActivity;
import com.example.library.logger.Logger;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.datasource.SubscriberDataSource;
import com.example.library.rest.SubscriberPreferencyClientREST;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

// AsyncTask<Map => paramètres, Integer => progrès, Map => résultat>

public class SubscriberPreferencyAsynTask extends AsyncTask<Map<String, Object>, Integer, Map<String, Object>> {

    private final static String LOG_TAG = SubscriberPreferencyAsynTask.class.getName();
    private SubscriberFragmentActivity activity;

    @Override
    protected Map<String, Object> doInBackground(Map<String, Object>... data) {
    try {
            activity = (SubscriberFragmentActivity) data[0].get("activity"); 
            Map<String, String> preferencies = (Map<String, String>) data[0].get("preferencies");
            SubscriberPreferencyClientREST subscriberPreferencyClientREST = new SubscriberPreferencyClientREST();
            subscriberPreferencyClientREST.setActivity(activity);
            return subscriberPreferencyClientREST.postPreferencies(preferencies);
        } catch(Exception e) {
            Logger.addMessage(LOG_TAG, "An error occured on treating preferencies request : " + e.getMessage());
            Log.e(LOG_TAG, " An exception occured ", e);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) {
        Log.d(LOG_TAG, "Post execute activity");
        activity.doAfterPreferenciesPut(result);
    }

}