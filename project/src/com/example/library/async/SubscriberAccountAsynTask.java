package com.example.library.async;

import java.util.HashMap;
import java.util.Map;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.SubscriberFragmentActivity;
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

public class SubscriberAccountAsynTask extends AsyncTask<Map<String, Object>, Integer, Map<String, Object>> {
    private final static String LOG_TAG = SubscriberAccountAsynTask.class.getName();
    private String message;
    private Context context;
    private ProgressDialog progressDialog;
    private SubscriberFragmentActivity activity; 
    // Map... userData correspond au premier paramètre du AsyncTask<Map, Integer, Map>
    @Override
    protected Map<String, Object> doInBackground(Map<String, Object>... data) {
        progressDialog = (ProgressDialog) data[0].get("progressDialog");
        activity = (SubscriberFragmentActivity) data[0].get("activity");
        SubscriberPreferencyClientREST subscriberPreferencyClientREST = new SubscriberPreferencyClientREST();
        subscriberPreferencyClientREST.setActivity(activity);
        return subscriberPreferencyClientREST.getPreferencies(SubscriberPreferencyClientREST.TYPE_WEB_SERVICE);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) {
        Log.d(LOG_TAG, "Post execute activity");
        activity.doAfterCredentials(result);
    }

}