package com.ruitu.btchelper.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    /**
     * 判断网络连接是否连通
     * 
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isAvailable()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isWIFIConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
}
