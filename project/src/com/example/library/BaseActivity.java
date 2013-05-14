package com.example.library;
 
import java.util.Date;
import com.example.library.converter.UniversalConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.library.async.BorrowingReturnReminderAsynTask;
import com.example.library.context.MainApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.example.library.logger.Logger;
import com.example.library.model.datasource.MainDataSource;
import com.example.library.model.entity.Borrowing;
import com.example.library.rest.ClientREST;
public class BaseActivity extends FragmentActivity {
    private final static String LOG_TAG = BaseActivity.class.getName();
    protected String[] locales;
    private String newLangName;
    private int langPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkBorrowingsToReturn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.init();
        Logger.setActivity(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean result = false;
        if (menuItem.getItemId() == R.id.lang_change) {
            result = true;
            Log.d(LOG_TAG, "Clicked on menu item lang_change");

            Resources resources = getResources();
            locales = resources.getStringArray(R.array.langs);
            if (locales == null || locales.length == 0) {
                showErrorBox(resources.getString(R.string.no_langs_title), resources.getString(R.string.no_langs_msg), 
                resources.getString(R.string.dialog_error_refresh), resources.getString(R.string.dialog_error_quit));
            } else {
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View langModifierView = inflater.inflate(R.layout.switch_lang, (ViewGroup) null, true);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set title
                alertDialogBuilder.setTitle(resources.getString(R.string.lang_choose));

                // set view with form data 
                alertDialogBuilder.setView(langModifierView);
    
                final Context globalContext = this;
                final Spinner type = (Spinner) langModifierView.findViewById(R.id.newLangSwitched);
                type.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        Log.d(LOG_TAG, "Found spinner"+type);
                        newLangName = type.getSelectedItem().toString();
                        langPosition = position;
                        Log.d(LOG_TAG, "Selected item position is " + position);
                        Log.d(LOG_TAG, "Found lang name" + newLangName);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {}
                });

                // set dialog message
                alertDialogBuilder
               .setMessage(resources.getString(R.string.lang_select))
                .setCancelable(true)
                .setPositiveButton(resources.getString(R.string.form_submit),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {    
                        dialog.cancel();
                        Locale locale = new Locale(locales[langPosition]);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        globalContext.getResources().updateConfiguration(config, globalContext.getResources().getDisplayMetrics());
                        refreshActivity();
                    }
                })
                .setNegativeButton(resources.getString(R.string.form_cancel),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
       }
        return result; 
    }

    /**
     * Looks for user borrowings waiting for to be returned. Check is done only once by day.
     * @access public
     * @return void
     */
    public void checkBorrowingsToReturn() {
        // do only when user put a correct API credentials
        MainApplication app = (MainApplication) getApplication();
        Log.d(LOG_TAG, "App state is :"+app.canDoBorrowingCheck());
        if (app.canDoBorrowingCheck()) {
            Log.d(LOG_TAG, "Checking at the first time");
            Map<String, Object> userParams = new HashMap<String, Object>();
            userParams.put("activity", this);
            BorrowingReturnReminderAsynTask borrowingReturnReminderAsynTask = new BorrowingReturnReminderAsynTask();
            borrowingReturnReminderAsynTask.execute(userParams);
            app.setLastCheckedDate(new Date());
        }
    }
    public void handleBorrowingsToReturnResponse(Map<String, Object> result) {
        Log.d(LOG_TAG, "Received response " + result);
        if (result != null && !handleResponseType((Map<String, String>) result.get(ClientREST.KEY_STATE))) {
            Log.d(LOG_TAG, "Borrowing returned checked; result is"+result);
            List<Borrowing> borrowings = (List<Borrowing>) result.get("borrowings");
            String message = getResources().getString(R.string.borrowing_to_return);
            if (borrowings == null || borrowings.size() == 0) {
                message = getResources().getString(R.string.borrowing_nothing_to_return);
            } else {
                Date biggestDate = null;
                for (Borrowing borrowing : borrowings) {
                    if (biggestDate == null || borrowing.getReturnDate().before(biggestDate)) {
                        biggestDate = borrowing.getReturnDate();
                    }
                }
                Map<String, String> config = (Map<String, String>) result.get("config");
                message = String.format(message, ""+borrowings.size(), UniversalConverter.                fromDateToString(biggestDate, config.get("date-format")));
            }
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            if (toast == null) Log.d(LOG_TAG, "Toast is null");
            else toast.show();
        }
    }

    public void handleBorrow(Map<String, Object> result) {
        Log.d(LOG_TAG, "Received response " + result + " and state " + result.get(ClientREST.KEY_STATE));
        if (!handleResponseType((Map<String, String>) result.get(ClientREST.KEY_STATE))) {
            String message = getResources().getString(R.string.borrowing_success);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Reloads current activity.
     * @access public
     * @return void
     */
    public void refreshActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    /**
     * Shows error dialog box.
     * @access public
     * @return void
     */
    public void showErrorBox(String title, String message, String positiveButtonTxt, String negativeButton) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogErrorView = inflater.inflate(R.layout.dialog_error, (ViewGroup) null, true);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(title);

        // set view with form data 
        alertDialogBuilder.setView(dialogErrorView);

        // set dialog message
        alertDialogBuilder
        .setMessage(message)
        .setCancelable(true)
        .setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
                refreshActivity();
            }
        })
        .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Handle the response of web service. 
     * @access protected
     * @param Map<String, String> response Response header returned by web service.
     * @return boolean True if the response contains an error. False otherwise.
     */
    protected boolean handleResponseType(Map<String, String> response) {
        if (response.get("state").equals("error")) {
            Toast.makeText(this, response.get("message"), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "Destroying activity");
        if (MainDataSource.getDatabaseInstance() != null) { 
            Log.d(LOG_TAG, "Database is opened. Close it");
            MainDataSource.close();
        }
        Logger.sendPending();
        super.onDestroy();
    }
}
