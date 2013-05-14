package com.example.library;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.example.library.async.BorrowingReturnReminderAsynTask;
import com.example.library.async.SubscriberAccountAsynTask;
import com.example.library.async.SubscriberPreferencyAsynTask;
import com.example.library.fragment.BookFragment;
import com.example.library.fragment.SubscriberAccountFragment;
import com.example.library.fragment.SubscriberWaitingListFragment;
import com.example.library.fragment.SubscriberPreferencyFragment;
import com.example.library.logger.Logger;
import com.example.library.model.datasource.CustomizationDataSource;
import com.example.library.model.datasource.SubscriberDataSource;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.entity.TabInfo;
import com.example.library.rest.ClientREST;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.Toast;

public class SubscriberFragmentActivity extends BaseFragmentActivity {

    private static final String LOG_TAG = SubscriberFragmentActivity.class.getName();
    private static final String TAB_ACCOUNT = "Account";  
    private static final String TAB_PREFERENCIES = "Preferencies";
    private static final String TAB_WAITING_LIST = "WaitingList";
    private String titleCredentials, titlePreferencies, titleWaitingList;
    private int choosedFrequency;
    private LinearLayout manualContainer;
    private int radioAutomaticId;
    private int autoManualChoice;
    private EditText refererUriEditText, aboutEditText;
    private DatePicker syncDate;
    private RadioGroup syncTypes;
    private Button submitCredentials, submitPreferencies;
    private ProgressDialog progressDialog;
    private Subscriber subscriber;
    public Map<String, String> preferencies;
    public SubscriberDataSource subscriberDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.my_account);
            subscriberDataSource = new SubscriberDataSource(this);
            Resources resources = getResources();
            titleCredentials = resources.getString(R.string.account_credentials_title);
            titlePreferencies = resources.getString(R.string.account_preferencies_title);
            titleWaitingList = resources.getString(R.string.account_waiting_list_title);
            initialiseTabHost(savedInstanceState); 
        }
        catch (Exception e) {
            Logger.addMessage(LOG_TAG, e.getMessage());
            Log.e(LOG_TAG, "An exception was catched", e);
        }
    }

    private void initialiseTabHost(Bundle savedInstanceState) {
        subscriber = subscriberDataSource.getSubscriber();
        Log.d(LOG_TAG, "Found subscriber on loading" + subscriber);
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        addTab(this, tabHost, tabHost.newTabSpec(TAB_ACCOUNT).setIndicator(titleCredentials), new TabInfo(TAB_ACCOUNT, SubscriberAccountFragment.class, savedInstanceState, this, ""));
        addTab(this, tabHost, tabHost.newTabSpec(TAB_WAITING_LIST).setIndicator(titleWaitingList), new TabInfo(TAB_WAITING_LIST, SubscriberWaitingListFragment.class, savedInstanceState, this, ""));
        if (subscriber != null) {
            showCredentialsDialog();
            submitCredentials();
        } else  { 
            onTabChanged(TAB_ACCOUNT);
        }
        // Default to first tab
        tabHost.setOnTabChangedListener(this);
    }

    public void handleForm(View view) {
        if (submitPreferencies == null) submitPreferencies = (Button) view;
        submitPreferencies.setEnabled(false);

        Resources resources = getResources();
        String dialogTitle = resources.getString(R.string.loading_title);
        String dialogMessage = resources.getString(R.string.my_preferencies_saving);
        progressDialog = ProgressDialog.show(this, dialogTitle, dialogMessage, true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface listener) {
                submitCredentials.setEnabled(true);
            }
        });

        View rootView = view.getRootView();
        refererUriEditText = (EditText) rootView.findViewById(R.id.prefRefererUri);
        aboutEditText = (EditText) rootView.findViewById(R.id.prefAbout);
        syncTypes = (RadioGroup) rootView.findViewById(R.id.prefBorrowingNotif);
        syncDate = (DatePicker) rootView.findViewById(R.id.prefSyncManualDate);

        String syncType = "AUTOMATIC";
        String syncDateTxt = "";
        if(syncTypes.getCheckedRadioButtonId() != radioAutomaticId) {
            syncType = "MANUAL";
            syncDateTxt = syncDate.getYear()+"-"+syncDate.getMonth()+"-"+syncDate.getDayOfMonth();
        }
        Map<String, String> preferencies = new HashMap<String, String>();
        preferencies.put("ABOU", aboutEditText.getText().toString());
        preferencies.put("FROM", refererUriEditText.getText().toString());
        preferencies.put("SYNC", syncType);
        preferencies.put("SYDA", syncDateTxt); // first synchronisation date

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("activity", this);
        params.put("preferencies", preferencies);
        SubscriberPreferencyAsynTask subscriberPreferencyAsynTask = new SubscriberPreferencyAsynTask();
        subscriberPreferencyAsynTask.execute(params);
    }

    public void handleMyAccountForm(View view) {
        if (submitCredentials == null) submitCredentials = (Button) view;
        submitCredentials.setEnabled(false);

        showCredentialsDialog();

        // get and save credentials
        View rootView = view.getRootView();
        EditText loginEditText = (EditText) rootView.findViewById(R.id.myAccountLogin);
        EditText passwordEditText = (EditText) rootView.findViewById(R.id.myAccountPassword);

        Map<String, Object> config = new HashMap<String, Object>();
        config.put("login", loginEditText.getText().toString());
        config.put("password", passwordEditText.getText().toString());

        subscriberDataSource.insertNewConfiguration(config);
        submitCredentials();
    }

    private void submitCredentials() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("activity", this);
        SubscriberAccountAsynTask subscriberAccountAsynTask = new SubscriberAccountAsynTask();
        subscriberAccountAsynTask.execute(params);
    }

    public void doAfterAsync() {
        Log.d(LOG_TAG, "mapTabInfo contains "+mapTabInfo);
        TabInfo tabInfoPreferencies = new TabInfo(TAB_PREFERENCIES, SubscriberPreferencyFragment.class, null, this, "");
        if (!mapTabInfo.containsKey(tabInfoPreferencies.getTag())) addTab(this, tabHost, tabHost.newTabSpec(TAB_PREFERENCIES).setIndicator(titlePreferencies), tabInfoPreferencies);
        onTabChanged(TAB_PREFERENCIES);
        if (submitCredentials != null) submitCredentials.setEnabled(true);
    }

    public void doAfterCredentials(Map<String, Object> result) {
        progressDialog.dismiss();
        if (!handleResponseType((Map<String, String>) result.get(ClientREST.KEY_STATE))) {
            // save preferencies
            preferencies = (Map<String, String>) result.get("preferencies");
            Log.d(LOG_TAG, "PReferencies are " + preferencies);
            doAfterAsync();
        }
    }

    public void doAfterPreferenciesPut(Map<String, Object> result) {
        Log.d(LOG_TAG, "Got preferencies " + result);
        progressDialog.dismiss();
        submitPreferencies.setEnabled(true);
        if (!handleResponseType((Map<String, String>) result.get(ClientREST.KEY_STATE))) {
            Toast.makeText(this, getResources().getString(R.string.success_preferencies_save), Toast.LENGTH_LONG).show();
        }
    }

    private void showCredentialsDialog() {
        Resources resources = getResources();
        String dialogTitle = resources.getString(R.string.loading_title);
        String dialogMessage = resources.getString(R.string.my_account_progress);

        progressDialog = ProgressDialog.show(this, dialogTitle, dialogMessage, true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface listener) {
                submitCredentials.setEnabled(true);
            } 
        });
    }

}