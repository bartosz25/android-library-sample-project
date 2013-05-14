package com.example.library.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.library.R;
import com.example.library.SubscriberFragmentActivity;
import com.example.library.adapter.LetterAdapter;
import com.example.library.listener.EditTextTouchListener;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Letter;
import com.example.library.model.entity.Subscriber;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.MotionEvent;

public class SubscriberAccountFragment extends Fragment {

    private static final String LOG_TAG = SubscriberAccountFragment.class.getName(); 
    private EditText login, password;

    @Override
    // Crée la vue associée au fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        Log.d(LOG_TAG, "Getting layout for SubscriberAccountFragment");
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.my_account_form, container, false);

        login =  (EditText) layout.findViewById(R.id.myAccountLogin);
        //login.setOnTouchListener(new EditTextTouchListener());
        password = (EditText) layout.findViewById(R.id.myAccountPassword);
        //password.setOnTouchListener(new EditTextTouchListener());

        Subscriber subscriber = ((SubscriberFragmentActivity) getActivity()).subscriberDataSource.getSubscriber();
        if (subscriber != null) {
            login.setText(subscriber.getLogin());
            password.setText(subscriber.getPassword());
        }

        return layout;
    }

}