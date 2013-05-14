package com.example.library.receiver;

import com.example.library.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WifiConnectionReceiver extends BroadcastReceiver {

    private final static String LOG_TAG = WifiConnectionReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Entered to WifiConnectionReceiver with intent : "+ intent.toString());
        
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Log.d(LOG_TAG, "WiFi is enabled ? : " + wifiManager.isWifiEnabled());
        //boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        //Log.d(LOG_TAG, "Has no connectivity " + noConnectivity);
        //Log.d(LOG_TAG, "Got action " + intent.getAction());
        //boolean stateChanged = (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION) || (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)));
        if (!wifiManager.isWifiEnabled()) {
            Log.d(LOG_TAG, "Connection was lost");
            Toast.makeText(context, context.getResources().getString(R.string.error_wifi_connection), Toast.LENGTH_LONG).show();
        } else {
            Log.d(LOG_TAG, "Connection was etablished");
        }
    }

}