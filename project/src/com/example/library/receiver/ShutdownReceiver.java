package com.example.library.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// TODO : supprimer celui-là
public class ShutdownReceiver extends BroadcastReceiver {

	private final static String LOG_TAG = ShutdownReceiver.class.getName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Application was ended");
	}

}
