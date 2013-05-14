package com.example.library;
 
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.util.Log;

public class MainActivity extends BaseActivity {

    private final static String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToMyAccount(View view) {
        Log.d(LOG_TAG, "Clicked on goToMyAccount");
        Intent intentMyAccount = new Intent(getApplicationContext(), SubscriberFragmentActivity.class);
        startActivity(intentMyAccount);
    }

    public void goToBooksList(View view) {
        Log.d(LOG_TAG, "Clicked on goToBooksList");
        Intent intentBooksList = new Intent(getApplicationContext(), BookFragmentActivity.class);
        startActivity(intentBooksList);
    }

    public void goToWebapp(View view) {
        Log.d(LOG_TAG, "Clicked on goToWebapp");
        Intent intentDisplayWebsite = new Intent(getApplicationContext(), DisplayWebsiteActivity.class);
        startActivity(intentDisplayWebsite);
    }

    public void goToContactForm(View view) {
        Log.d(LOG_TAG, "Clicked on goToContactForm");
        Intent intentForm = new Intent(getApplicationContext(), ContactFormActivity.class);
        startActivity(intentForm);
    }

    public void goToCustomize(View view) {
        Log.d(LOG_TAG, "Clicked on goToCustomize");
        Intent intentCustomize = new Intent(getApplicationContext(), CustomizeActivity.class);
        startActivity(intentCustomize);
    }

}