package com.alibaba.sdk.android.openaccount.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.alibaba.sdk.android.openaccount.network.ConnectType;
import com.alibaba.sdk.android.openaccount.network.MobileNetworkType;

/* JADX INFO: loaded from: classes.dex */
public class NetworkUtils {
    public static MobileNetworkType getMobileNetworkType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.getType() == 0 && activeNetworkInfo.isConnected()) {
            return getNetWorkType(activeNetworkInfo.getSubtype());
        }
        return MobileNetworkType.MOBILE_NETWORK_TYPE_UNKNOWN;
    }

    private static MobileNetworkType getNetWorkType(int i) {
        switch (i) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return MobileNetworkType.MOBILE_NETWORK_TYPE_2G;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return MobileNetworkType.MOBILE_NETWORK_TYPE_3G;
            case 13:
                return MobileNetworkType.MOBILE_NETWORK_TYPE_4G;
            default:
                return MobileNetworkType.MOBILE_NETWORK_TYPE_UNKNOWN;
        }
    }

    public static ConnectType getConnectType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            switch (activeNetworkInfo.getType()) {
                case 0:
                    return ConnectType.CONNECT_TYPE_MOBILE;
                case 1:
                    return ConnectType.CONNECT_TYPE_WIFI;
                default:
                    return ConnectType.CONNECT_TYPE_OTHER;
            }
        }
        return ConnectType.CONNECT_TYPE_DISCONNECT;
    }

    public static boolean isNetworkAvaiable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    public static String getLocalMacAddress(Context context) {
        WifiInfo connectionInfo;
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager == null || (connectionInfo = wifiManager.getConnectionInfo()) == null) {
            return null;
        }
        return connectionInfo.getMacAddress();
    }
}
