package com.example.library;

import java.util.HashMap;
import java.util.Map;

import com.example.library.async.BorrowingReturnReminderAsynTask;
import com.example.library.context.MainApplication;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//import gueei.binding.app.*;

public class ContactFormActivity extends BaseActivity {
    private final static String LOG_TAG = ContactFormActivity.class.getName();
    private final static int EMAIL_SENT = 1;
    private boolean hasErrors = false;
    private boolean formWasSubmitted = false;
    private TextView errorSpinnerContainerView;
    private Spinner spinnerChoice;
    private String defaultDemandType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Creating ContactFormActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form); 
        defaultDemandType = getResources().getString(R.string.contact_select_item);
        errorSpinnerContainerView = (TextView) findViewById(R.id.mailCategoryError);
        spinnerChoice = ((Spinner) findViewById(R.id.mailMessageType));
        spinnerChoice.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                isValidSpinnerChoice(null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    } 

    public void handleForm(View view) {
        Log.d(LOG_TAG, "Handling contact form");
        hasErrors = false;
        formWasSubmitted = true;

        EditText title = (EditText) findViewById(R.id.mailTitle);
        EditText content =  (EditText) findViewById(R.id.mailContent);
        EditText mail = (EditText) findViewById(R.id.mailAddress);
        String demandType = spinnerChoice.getSelectedItem().toString();

        if (title.getText() == null || (title.getText() != null && (title.getText().toString().equals("") || title.getText().toString().length() < 10))) {
            title.setError(String.format(getResources().getString(R.string.error_contact_title), 10));
            hasErrors = true;
        }

        if (content.getText() == null || (content.getText() != null && (content.getText().toString().equals("") || content.getText().toString().length() < 20))) {
            content.setError(String.format(getResources().getString(R.string.error_contact_content), 20));
            hasErrors = true;
        }
        isValidSpinnerChoice(spinnerChoice);

        Log.d("mail", mail.getText().toString());
        Log.d("demandType", demandType);
        Log.d("test", Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()+"");
        if (mail.getText() == null || (mail.getText() != null && !Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches())) {
            mail.setError(getResources().getString(R.string.error_contact_email));
            hasErrors = true;
        }

        if (!hasErrors) {
            StringBuffer message = new StringBuffer();
            message.append("Type of demand : ");
            message.append("\n");
            message.append(demandType);
            message.append("Content : ");
            message.append("\n");
            message.append(content.getText().toString());
            message.append("Sender :");
            message.append("\n");
            message.append(mail.getText().toString());
            // TODO : (plus tard) tester si cela envoie vraiement l'e-mail
            Log.d(LOG_TAG, "No errors found. Send an e-mail.");
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{MainApplication.SENDER_MAIL, mail.getText().toString()});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString());
            emailIntent.putExtra(Intent.EXTRA_TEXT, message.toString());
            emailIntent.setType("message/rfc882");
            startActivityForResult(Intent.createChooser(emailIntent, getResources().getString(R.string.email_select_client)), EMAIL_SENT);
        }
    }

    private void isValidSpinnerChoice(Spinner spinner) {
        if (formWasSubmitted) {
            String demandType;
            if(spinner != null) demandType = spinner.getSelectedItem().toString();
            else demandType = spinnerChoice.getSelectedItem().toString();
            if (demandType == null || (demandType.trim().equals(defaultDemandType))) {
                errorSpinnerContainerView.setVisibility(View.VISIBLE);
                errorSpinnerContainerView.setError(defaultDemandType);
            } else {
                errorSpinnerContainerView.setVisibility(View.INVISIBLE);
            }
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String message = "";
        Log.d(LOG_TAG, "onActivityResult called with params " + requestCode + ", " + resultCode + ", "+ data);
        Log.d(LOG_TAG, "RESULT_OK is "+ RESULT_OK);
        Log.d(LOG_TAG, "RESULT_CANCELED is " + RESULT_CANCELED);
        if (requestCode == EMAIL_SENT) {
            if (resultCode == RESULT_OK) {
                message = getResources().getString(R.string.mail_sent_ok);
            } else {
                message = getResources().getString(R.string.mail_sent_error);
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}