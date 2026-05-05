package com.aliyun.alink.linksdk.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/* JADX INFO: loaded from: classes2.dex */
public class NetTools {
    public static boolean isAvailable(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        return (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isAvailable() || !activeNetworkInfo.isConnectedOrConnecting()) ? false : true;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        return (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || activeNetworkInfo.getType() != 1) ? false : true;
    }

    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        return (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || activeNetworkInfo.getType() != 0) ? false : true;
    }

    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager;
        if (context == null || (wifiManager = (WifiManager) context.getSystemService("wifi")) == null) {
            return null;
        }
        return wifiManager.getConnectionInfo();
    }

    public static String getWifiSSID(Context context) {
        WifiInfo wifiInfo;
        String ssid;
        if (context == null || (wifiInfo = getWifiInfo(context)) == null || (ssid = wifiInfo.getSSID()) == null) {
            return null;
        }
        try {
            byte[] bytes = ssid.replace("\"", "").getBytes();
            if (bytes != null && bytes.length > 0) {
                String str = new String(bytes, "UTF-8");
                if (str.equals("<unknown ssid>")) {
                    return null;
                }
                if (str.equals("0x")) {
                    return null;
                }
                return str;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
