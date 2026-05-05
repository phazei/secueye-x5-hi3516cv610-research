package com.alibaba.sdk.android.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class NetworkUtils {
    private static boolean checkHasPermission(Context context, String str) {
        Class<?> cls;
        try {
            cls = Class.forName("androidx.core.content.ContextCompat");
        } catch (Exception unused) {
            cls = null;
        }
        if (cls == null) {
            try {
                cls = Class.forName("androidx.core.content.ContextCompat");
            } catch (Exception unused2) {
            }
        }
        if (cls == null) {
            return true;
        }
        return ((Integer) cls.getMethod("androidx.core.content.ContextCompat", Context.class, String.class).invoke(null, context, str)).intValue() == 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getMobileNetworkType(android.content.Context r2, android.telephony.TelephonyManager r3, android.net.ConnectivityManager r4) {
        /*
            r0 = 30
            if (r3 == 0) goto L28
            int r1 = android.os.Build.VERSION.SDK_INT
            if (r1 < r0) goto L1b
            java.lang.String r1 = "android.permission.READ_PHONE_STATE"
            boolean r1 = checkHasPermission(r2, r1)
            if (r1 != 0) goto L16
            boolean r1 = r3.hasCarrierPrivileges()
            if (r1 == 0) goto L1b
        L16:
            int r2 = r3.getDataNetworkType()
            goto L29
        L1b:
            java.lang.String r1 = "android.permission.READ_PHONE_STATE"
            boolean r2 = checkHasPermission(r2, r1)
            if (r2 == 0) goto L28
            int r2 = r3.getNetworkType()     // Catch: java.lang.Exception -> L28
            goto L29
        L28:
            r2 = 0
        L29:
            if (r2 != 0) goto L3e
            int r3 = android.os.Build.VERSION.SDK_INT
            if (r3 < r0) goto L32
            java.lang.String r2 = "NULL"
            return r2
        L32:
            if (r4 == 0) goto L3e
            android.net.NetworkInfo r3 = r4.getActiveNetworkInfo()
            if (r3 == 0) goto L3e
            int r2 = r3.getSubtype()
        L3e:
            switch(r2) {
                case 1: goto L4d;
                case 2: goto L4d;
                case 3: goto L4a;
                case 4: goto L4d;
                case 5: goto L4a;
                case 6: goto L4a;
                case 7: goto L4d;
                case 8: goto L4a;
                case 9: goto L4a;
                case 10: goto L4a;
                case 11: goto L4d;
                case 12: goto L4a;
                case 13: goto L47;
                case 14: goto L4a;
                case 15: goto L4a;
                case 16: goto L41;
                case 17: goto L41;
                case 18: goto L47;
                case 19: goto L47;
                case 20: goto L44;
                default: goto L41;
            }
        L41:
            java.lang.String r2 = "NULL"
            return r2
        L44:
            java.lang.String r2 = "5G"
            return r2
        L47:
            java.lang.String r2 = "4G"
            return r2
        L4a:
            java.lang.String r2 = "3G"
            return r2
        L4d:
            java.lang.String r2 = "2G"
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.tool.NetworkUtils.getMobileNetworkType(android.content.Context, android.telephony.TelephonyManager, android.net.ConnectivityManager):java.lang.String");
    }

    public static String getWifiAddress(Context context) {
        WifiInfo connectionInfo;
        if (context == null || (connectionInfo = ((WifiManager) context.getApplicationContext().getSystemService("wifi")).getConnectionInfo()) == null) {
            return "00:00:00:00:00:00";
        }
        String macAddress = connectionInfo.getMacAddress();
        return TextUtils.isEmpty(macAddress) ? "00:00:00:00:00:00" : macAddress;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isWiFiNetwork(ConnectivityManager connectivityManager) {
        NetworkCapabilities networkCapabilities;
        if (Build.VERSION.SDK_INT < 23) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)) == null) {
            return false;
        }
        return networkCapabilities.hasTransport(1);
    }
}
