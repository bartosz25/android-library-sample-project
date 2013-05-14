package com.example.library;
 
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


// la classe n'est plus utilisée depuis le passage en TabHost
@Deprecated
public class WebServicePreferencyActivity extends BaseActivity  {
    
	private final static String LOG_TAG = "WebServicePreferencyActivity"; 
    private int choosedFrequency;
	private LinearLayout manualContainer;
	private int radioAutomaticId;
	private int autoManualChoice;
	private EditText refererUriEditText, aboutEditText;
	private DatePicker syncDate;
	private RadioGroup syncTypes;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Creating WebServicePreferencyActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_service_preferency); 
        
        refererUriEditText = (EditText) findViewById(R.id.prefRefererUri);
    	aboutEditText = (EditText) findViewById(R.id.prefAbout);
    	
        radioAutomaticId = R.id.radioAutomatic;
        refererUriEditText.setText("http://test.com");
        aboutEditText.setText("About me");
        syncTypes = (RadioGroup) findViewById(R.id.prefBorrowingNotif);
        syncTypes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton checkedButton = (RadioButton) findViewById(checkedId);
				if(checkedId == radioAutomaticId) hideManuallyGroup();
				else showManuallyGroup();				
			} 
        	
        });
        
        RadioButton buttonAutomatic = (RadioButton) findViewById(radioAutomaticId);
        //buttonAutomatic.setChecked(true);
        manualContainer = (LinearLayout) findViewById(R.id.prefManualyContainer);
        if(!buttonAutomatic.isChecked()) showManuallyGroup();
        else hideManuallyGroup();
        
        syncDate = (DatePicker) findViewById(R.id.prefSyncManualDate);
        // get tomorrow date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        syncDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        
        Spinner syncFrequency = (Spinner) findViewById(R.id.prefNotifFrequency);
        syncFrequency.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long row) {
				    choosedFrequency = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
        	
        });
        syncFrequency.setSelection(choosedFrequency);
        
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void handleForm(View view) {
    	String refererUri = refererUriEditText.getText().toString();
    	String about = aboutEditText.getText().toString();
    	
    	if(syncTypes.getCheckedRadioButtonId() != radioAutomaticId) {
        	// choosedFrequency utilisé pour soumettre les informations au web service
        	Spinner frequencySpinner = ((Spinner) findViewById(R.id.prefNotifFrequency));
        	String frequency = frequencySpinner.getSelectedItem().toString();
        	
        	Calendar syncCalendar = Calendar.getInstance();
        	syncCalendar.set(Calendar.YEAR, syncDate.getYear());
        	syncCalendar.set(Calendar.MONTH, syncDate.getMonth());
        	syncCalendar.set(Calendar.DAY_OF_MONTH, syncDate.getDayOfMonth());

        	Log.d(LOG_TAG, "Date " + syncCalendar);
        	Log.d(LOG_TAG, "Frequency " + frequency);
    	}
    	Log.d(LOG_TAG, "Referer URI " + refererUri);
    	Log.d(LOG_TAG, "About " + about);
    	Log.d(LOG_TAG, "Choosen option " + syncTypes.getCheckedRadioButtonId());
    }
    
    private void showManuallyGroup() {
    	manualContainer.setVisibility(View.VISIBLE);
    	manualContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }
    private void hideManuallyGroup() {
    	manualContainer.setVisibility(View.INVISIBLE);
    	manualContainer.setLayoutParams(new LayoutParams(0, 0));
    }
    
    
}