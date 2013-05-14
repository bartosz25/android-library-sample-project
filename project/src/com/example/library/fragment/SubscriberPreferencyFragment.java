package com.example.library.fragment;

import java.util.Calendar;
import java.util.Map;

import com.example.library.R;
import com.example.library.converter.UniversalConverter;
import com.example.library.listener.EditTextTouchListener;
import com.example.library.model.datasource.PreferencyDataSource;
import com.example.library.rest.SubscriberPreferencyClientREST;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import com.example.library.SubscriberFragmentActivity;

public class SubscriberPreferencyFragment extends Fragment {

    private static final String LOG_TAG = SubscriberPreferencyFragment.class.getName(); 
    private int choosedFrequency;
    private LinearLayout manualContainer;
    private int radioAutomaticId;
    private int autoManualChoice;
    private EditText refererUriEditText, aboutEditText;
    private DatePicker syncDate;
    private RadioGroup syncTypes;
    private PreferencyDataSource preferencyDataSource;
    private RadioButton buttonManual, buttonAutomatic;

    @Override
    // Crée la vue associée au fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        final ScrollView fragmentLayout = (ScrollView) inflater.inflate(R.layout.web_service_preferency, container, false);

        refererUriEditText = (EditText) fragmentLayout.findViewById(R.id.prefRefererUri);
        //refererUriEditText.setOnTouchListener(new EditTextTouchListener());
        aboutEditText = (EditText) fragmentLayout.findViewById(R.id.prefAbout);
        //aboutEditText.setOnTouchListener(new EditTextTouchListener());
        radioAutomaticId = R.id.radioAutomatic;

        // Si données dans la base = il s'agit des données insérées après la saisie des identifiants
        // Sinon = identifiants sont corrects, données étaient supprimées, on peut donc appeler web service pour obtenir les informations les plus récéntes
        Map<String, String> prefs = ((SubscriberFragmentActivity) getActivity()).preferencies; //preferencyDataSource.getPreferencies();
        Log.d(LOG_TAG, "Preferencies found " + prefs);

        manualContainer = (LinearLayout) fragmentLayout.findViewById(R.id.prefManualyContainer);
        refererUriEditText.setText(prefs.get("FROM"));
        aboutEditText.setText(prefs.get("ABOU"));

        buttonManual = (RadioButton) fragmentLayout.findViewById(R.id.radioManual);
        buttonAutomatic = (RadioButton) fragmentLayout.findViewById(radioAutomaticId);
        if(prefs.get("SYMA") != null && !prefs.get("SYMA").trim().equals("")) {
            buttonManual.setChecked(true);
            showManuallyGroup();
        } else {
            buttonAutomatic.setChecked(true);
            hideManuallyGroup();
        }

        syncTypes = (RadioGroup) fragmentLayout.findViewById(R.id.prefBorrowingNotif);
        syncTypes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton = (RadioButton) fragmentLayout.findViewById(checkedId);
                if(checkedId == radioAutomaticId) hideManuallyGroup();
                else showManuallyGroup();				
            }
        });

        syncDate = (DatePicker) fragmentLayout.findViewById(R.id.prefSyncManualDate);
        // get tomorrow date = default date is always tomorrow
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        syncDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        if(prefs.containsKey("SYNC")) {
            choosedFrequency = UniversalConverter.fromStringToInt(prefs.get("SYNC"));
            Log.d(LOG_TAG, "Put new choosedFrequency " + choosedFrequency);
        }

        Spinner syncFrequency = (Spinner) fragmentLayout.findViewById(R.id.prefNotifFrequency);
        syncFrequency.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long row) {
                choosedFrequency = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        syncFrequency.setSelection(choosedFrequency);
        return fragmentLayout;
    }

    private void showManuallyGroup() {
        manualContainer.setVisibility(View.VISIBLE);
        manualContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void hideManuallyGroup() {
        manualContainer.setVisibility(View.INVISIBLE);
        manualContainer.setLayoutParams(new LayoutParams(0, 0));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        preferencyDataSource = new PreferencyDataSource(activity);
    }

}