package com.alibaba.ailabs.tg.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import com.hjq.permissions.Permission;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class NetworkUtils {
    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_IWLAN = 18;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;

    public enum NetworkType {
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    private static boolean is24GHz(int i) {
        return i > 2400 && i < 2500;
    }

    public static boolean is5GHz(int i) {
        return i > 4900 && i < 5900;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo;
        if (context == null || (activeNetworkInfo = getActiveNetworkInfo(context)) == null) {
            return false;
        }
        return activeNetworkInfo.isAvailable();
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo networkInfo;
        if (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null || (networkInfo = connectivityManager.getNetworkInfo(1)) == null) {
            return false;
        }
        return networkInfo.isAvailable() || networkInfo.isConnected();
    }

    public static boolean isWifiEnable(Context context) {
        WifiManager wifiManager;
        return (context == null || (wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi")) == null || !wifiManager.isWifiEnabled()) ? false : true;
    }

    @RequiresPermission(allOf = {"android.permission.ACCESS_WIFI_STATE", "android.permission.ACCESS_NETWORK_STATE"})
    public static String getCurrentWifiSsid(Context context, boolean z) {
        WifiManager wifiManager;
        WifiInfo connectionInfo;
        if (context == null || (wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi")) == null) {
            return null;
        }
        if ((!isWifiAvailable(context) && z) || (connectionInfo = wifiManager.getConnectionInfo()) == null) {
            return null;
        }
        String ssid = connectionInfo.getSSID();
        return ssid != null ? ssid.replaceAll("\"", "") : ssid;
    }

    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public static String getWifiFrequency(Context context, String str) {
        List<ScanResult> wifiScanResult = getWifiScanResult(context);
        if (wifiScanResult == null) {
            return "unknown";
        }
        int size = wifiScanResult.size();
        for (int i = 0; i < size; i++) {
            ScanResult scanResult = wifiScanResult.get(i);
            if (scanResult == null || scanResult.SSID == null) {
                return "unknown";
            }
            if (StringUtils.equals(scanResult.SSID, str)) {
                if (is5GHz(scanResult.frequency)) {
                    return "5GHz";
                }
                if (is24GHz(scanResult.frequency)) {
                    return "24GHz";
                }
            }
        }
        return "unknown";
    }

    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public static List<ScanResult> getWifiScanResult(Context context) {
        WifiManager wifiManager;
        if ((Build.VERSION.SDK_INT < 23 || (ContextCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) == 0 && ContextCompat.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION) == 0)) && (wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi")) != null && wifiManager.isWifiEnabled()) {
            return wifiManager.getScanResults();
        }
        return null;
    }

    public static void openWirelessSettings(@NonNull Context context) {
        context.startActivity(new Intent("android.settings.WIRELESS_SETTINGS").setFlags(268435456));
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean isMobileData(@NonNull Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.getType() == 0;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean is4G(@NonNull Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.getSubtype() == 13;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static NetworkType getNetworkType(@NonNull Context context) {
        NetworkType networkType = NetworkType.NETWORK_NO;
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return networkType;
        }
        if (activeNetworkInfo.getType() == 1) {
            return NetworkType.NETWORK_WIFI;
        }
        if (activeNetworkInfo.getType() == 0) {
            switch (activeNetworkInfo.getSubtype()) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                case 16:
                    return NetworkType.NETWORK_2G;
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                case 17:
                    return NetworkType.NETWORK_3G;
                case 13:
                case 18:
                    return NetworkType.NETWORK_4G;
                default:
                    String subtypeName = activeNetworkInfo.getSubtypeName();
                    if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) {
                        return NetworkType.NETWORK_3G;
                    }
                    return NetworkType.NETWORK_UNKNOWN;
            }
        }
        return NetworkType.NETWORK_UNKNOWN;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    private static NetworkInfo getActiveNetworkInfo(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
        if (connectivityManager == null) {
            return null;
        }
        return connectivityManager.getActiveNetworkInfo();
    }
}
