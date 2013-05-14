package com.example.library.async;

import java.util.HashMap;
import java.util.Map;

import com.example.library.MainActivity;
import com.example.library.OneBookActivity;
import com.example.library.R;
import com.example.library.SubscriberFragmentActivity;
import com.example.library.fragment.BookFragment;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.datasource.SubscriberDataSource;
import com.example.library.model.entity.Book;
import com.example.library.rest.LetterClientREST;
import com.example.library.rest.SubscriberPreferencyClientREST;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LettersGetterAsynTask extends AsyncTask<Map<String, Object>, Integer, Map<String, Object>> {
    private final static String LOG_TAG = LettersGetterAsynTask.class.getName();
    private BookFragment bookFragment; 
    private Activity activity;

    @Override
    protected Map<String, Object> doInBackground(Map<String, Object>... data) {
        activity = (Activity) data[0].get("activity");
        bookFragment = (BookFragment) data[0].get("bookFragment");
        LetterClientREST letterClientREST = new LetterClientREST();
        letterClientREST.setActivity(activity);
        return letterClientREST.getLetters((String) data[0].get("by"));
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) { 
        bookFragment.appendLetters(result);
    }

}