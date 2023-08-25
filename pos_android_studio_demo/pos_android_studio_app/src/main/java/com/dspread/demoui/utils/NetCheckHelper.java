package com.dspread.demoui.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetCheckHelper {

    //Determine whether the network connection is available (return true to indicate that the network is available, false to be unavailable)
    public static  boolean checkNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        //Get all mobile phone link management objects (including Wi-Fi, net and other connection management)
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        } else {
            //Gets the NetworkInfo object
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null && info.length > 0) {
                for (int i = 0; i < info.length; i++) {
                    // Determine whether the current network status is connected
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
