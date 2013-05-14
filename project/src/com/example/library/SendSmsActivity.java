package com.example.library;

import java.util.HashMap;
import java.util.Map;

import com.example.library.adapter.ContactAutocompleteAdapter;
import com.example.library.async.BorrowingReturnReminderAsynTask;
import com.example.library.context.MainApplication;
import com.example.library.logger.Logger;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//import gueei.binding.app.*;

public class SendSmsActivity extends BaseActivity {
    private final static String LOG_TAG = SendSmsActivity.class.getName();
    private long bookId = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Creating ContactFormActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_sms);

        Intent intent = getIntent();
        bookId = intent.getLongExtra("bookId", 0l);
        String bookTitle = intent.getStringExtra("bookTitle");
        Log.d(LOG_TAG, "Book id is " + bookId);

        Cursor contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        ContactAutocompleteAdapter contactAutocompleteAdapter = new ContactAutocompleteAdapter(this, contactCursor, false);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocompleteContactList);
        autoCompleteTextView.setAdapter(contactAutocompleteAdapter);
        autoCompleteTextView.setThreshold(2); // search after 2 caracters entered

        String message = getResources().getString(R.string.sms_content);
        String messageTemplate = String.format(message, bookTitle, intent.getStringExtra("bookUrl"));  
        ((TextView) findViewById(R.id.smsText)).setText(messageTemplate);
    }

    public void handleForm(View view) {
        Log.d(LOG_TAG, "Handling contact form");
        final EditText text = (EditText) findViewById(R.id.smsText);
        final AutoCompleteTextView number =  (AutoCompleteTextView) findViewById(R.id.autocompleteContactList);

        boolean hasErrors = false;
        if (text != null && (text.getText().toString().trim().equals("") || text.getText().toString().trim().length() < 10)) {
            text.setError("Title has to be at least 10 characters");
            hasErrors = true;
        }
        if (number != null && (number.getText().toString().trim().equals(""))) {
            number.setError("Phone number is obligatory field");
            hasErrors = true;
        }

        if (!hasErrors) {
            Resources resources = getResources();
            Log.d(LOG_TAG, "Sending message to " + number.getText().toString().trim() + "==> "  + text.getText().toString().trim());
            try {
                // Register receivers first; the first one is the receiver for SMS sending event (when SMS has been sent)
                // Code found here : http://stackoverflow.com/questions/8998655/unable-to-send-sms-using-smsmanager-in-android
                String smsSent = "SMS_SENT";
                PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(smsSent), 0);
                registerReceiver(new BroadcastReceiver() {
					@Override
                    public void onReceive(Context context, Intent intent) {
                        String message = null; Log.d(LOG_TAG, "RESULT CODE IS " + getResultCode());
                        switch (getResultCode()) {
                        case Activity.RESULT_OK :   
                            message = getResources().getString(R.string.sms_send_ok);
                            break;    
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE :   
                            message = getResources().getString(R.string.sms_send_error_generic);
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE :   
                            message = getResources().getString(R.string.sms_send_error_service);
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU :   
                            message = getResources().getString(R.string.sms_send_error_pdu);
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF :    
                            message = getResources().getString(R.string.sms_send_error_off);
                            break;
                        }
                        if (message != null) Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }, new IntentFilter(smsSent));
 
                // The second receivers is for SMS delivery
                String smsDelivery = "SMS_DELIVERED"; 
                PendingIntent deliveryPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(smsDelivery), 0);
                registerReceiver(new BroadcastReceiver() {
					@Override
                    public void onReceive(Context context, Intent intent) {
                        String message = null; Log.d(LOG_TAG, "Result code is " + getResultCode());
                        switch (getResultCode()) {
                        case Activity.RESULT_OK :   
                            message = getResources().getString(R.string.sms_delivery_ok);
                            break;    
                        case Activity.RESULT_CANCELED :   
                            message = getResources().getString(R.string.sms_delivery_error);
                            break;
                        }
                        if (message != null) Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }, new IntentFilter(smsDelivery));
                
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number.getText().toString().trim(), null, text.getText().toString().trim(), sentPendingIntent, deliveryPendingIntent);
                Log.d(LOG_TAG, "Message was send with smsManager : " + smsManager);
            } catch (Exception e) {
                Logger.addMessage(LOG_TAG, "An error occured on sending SMS : " + e.getMessage());
                Log.e(LOG_TAG, "An error occured on sending SMS", e);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.sms_send_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backToBook(View view) {
        Intent intent = new Intent(this, SendSmsActivity.class);
        intent.putExtra("bookId", bookId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}