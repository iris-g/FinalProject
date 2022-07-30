package com.example.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    Toast toast;
    int duration = Toast.LENGTH_SHORT;

    private static String Network="android.net.conn.CONNECTIVITY_CHANGE";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Network))
        {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED)
            {
                toast = Toast.makeText(context.getApplicationContext(),
                        "Network ON", Toast.LENGTH_LONG);
            }
            else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED)
            {
                toast = Toast.makeText(context.getApplicationContext(),
                        "Network OFF", Toast.LENGTH_LONG);
            }

            /**Setting the design of the toast message*/
            toast.show();
        }
    }
}