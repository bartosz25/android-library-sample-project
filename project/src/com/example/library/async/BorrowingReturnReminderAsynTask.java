package com.example.library.async;
import com.example.library.BaseActivity;
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

// AsyncTask<Map => paramètres, Integer => progrès, Map => résultat>
/**
 * 
 * L'utilité de cette classe est la suivante : 
 * - elle est lancée une fois par lancement de l'application
 * - le premier lancement affiche une popup avec la liste des livres à retourner
 * Dans un deuxième temps, on mettra en place les mêmes notifications que dans le cas de nouveaux mails Google
 */
public class BorrowingReturnReminderAsynTask extends AsyncTask<Map<String, Object>, Integer, Map<String, Object>> {
    private final static String LOG_TAG = BorrowingReturnReminderAsynTask.class.getName();
    private String message;
    private Context context;
    private BaseActivity activity;
    // Map... userData correspond au premier paramètre du AsyncTask<Map, Integer, Map>
    @Override
    protected Map<String, Object> doInBackground(Map<String, Object>... data) {
        activity = (BaseActivity) data[0].get("activity");
        BorrowingClientREST borrowingClientREST = new BorrowingClientREST();
        borrowingClientREST.setActivity(activity);
        return borrowingClientREST.getBorrowings(BorrowingClientREST.TYPE_TO_RETURN);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) {
        activity.handleBorrowingsToReturnResponse(result);
    }

}