package com.aliyun.alink.business.devicecenter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import com.aliyun.alink.business.devicecenter.log.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkTypeUtils {
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_5G = 5;
    public static final int NETWORN_MOBILE = 0;
    public static final int NETWORN_NONE = -1;
    public static final int NETWORN_WIFI = 1;

    public static boolean getMobileDataState(Context context, Object[] objArr) {
        try {
            if (!hasSimCard(context)) {
                return false;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            return ((Boolean) connectivityManager.getClass().getMethod("getMobileDataEnabled", objArr != null ? new Class[]{objArr.getClass()} : null).invoke(connectivityManager, objArr)).booleanValue();
        } catch (Exception unused) {
            return false;
        }
    }

    public static int getNetworkState(Context context) {
        NetworkInfo activeNetworkInfo;
        NetworkInfo.State state;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isAvailable()) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
            if (networkInfo != null && (state = networkInfo.getState()) != null && (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)) {
                return 1;
            }
            NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
            if (networkInfo2 != null) {
                NetworkInfo.State state2 = networkInfo2.getState();
                String subtypeName = networkInfo2.getSubtypeName();
                if (state2 != null && (state2 == NetworkInfo.State.CONNECTED || state2 == NetworkInfo.State.CONNECTING)) {
                    int subtype = activeNetworkInfo.getSubtype();
                    if (subtype == 20) {
                        return 5;
                    }
                    switch (subtype) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            return 2;
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 14:
                        case 15:
                            return 3;
                        case 13:
                            return 4;
                        default:
                            return (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) ? 3 : 0;
                    }
                }
            }
        }
        return -1;
    }

    public static boolean hasSimCard(Context context) {
        try {
            int simState = ((TelephonyManager) context.getSystemService("phone")).getSimState();
            return (simState == 0 || simState == 1) ? false : true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isMobileNetwork(Context context) {
        ConnectivityManager connectivityManager;
        ALog.d("NetworkTypeUtils", "isMobileNetworkConnected called.");
        if (context == null) {
            ALog.w("NetworkTypeUtils", "isMobileNetworkConnected context=null.");
            return false;
        }
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        } catch (Exception e) {
            ALog.w("NetworkTypeUtils", "isMobileNetworkConnected exception=" + e);
        }
        if (connectivityManager == null) {
            ALog.w("NetworkTypeUtils", "isMobileNetworkConnected ConnectivityManager=null");
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
        if (networkInfo == null) {
            ALog.w("NetworkTypeUtils", "isMobileNetworkConnected wifiInfo=null");
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("state=");
        sb.append(networkInfo.getState());
        ALog.d("NetworkTypeUtils", sb.toString());
        return networkInfo.getState() == NetworkInfo.State.CONNECTED || networkInfo.getState() == NetworkInfo.State.CONNECTING;
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null && (allNetworkInfo = connectivityManager.getAllNetworkInfo()) != null && allNetworkInfo.length > 0) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isWiFi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            ALog.w("NetworkTypeUtils", "isWiFi ConnectivityManager=null");
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
    }

    public static boolean isWifiNetwork(Context context) {
        ConnectivityManager connectivityManager;
        ALog.d("NetworkTypeUtils", "isWifiConnected called.");
        if (context == null) {
            ALog.w("NetworkTypeUtils", "isWifiConnected context=nul.l");
            return false;
        }
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        } catch (Exception e) {
            ALog.w("NetworkTypeUtils", "isWifiConnected exception=" + e);
        }
        if (connectivityManager == null) {
            ALog.w("NetworkTypeUtils", "isWifiConnected ConnectivityManager=null");
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        if (networkInfo == null || networkInfo.getState() == null) {
            ALog.w("NetworkTypeUtils", "isWifiConnected wifiInfo=null");
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("state=");
        sb.append(networkInfo.getState());
        ALog.d("NetworkTypeUtils", sb.toString());
        return networkInfo.getState() == NetworkInfo.State.CONNECTED || networkInfo.getState() == NetworkInfo.State.CONNECTING;
    }
}
