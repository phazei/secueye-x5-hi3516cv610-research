package com.aliyun.alink.business.devicecenter.utils;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import com.hjq.permissions.Permission;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionCheckerUtils {
    public static boolean checkPermission(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return Build.VERSION.SDK_INT < 23 ? context.getPackageManager().checkPermission(str, context.getPackageName()) == 0 : ContextCompat.checkSelfPermission(context, str) == 0;
    }

    public static boolean hasBleConnectPermission(Context context) {
        if (context == null) {
            return false;
        }
        Context applicationContext = context.getApplicationContext();
        if ((applicationContext instanceof Application ? applicationContext.getApplicationInfo().targetSdkVersion : 0) < 31 || Build.VERSION.SDK_INT < 31) {
            return true;
        }
        return checkPermission(context, Permission.BLUETOOTH_CONNECT);
    }

    public static boolean hasBleScanPermission(Context context) {
        if (context == null) {
            return false;
        }
        Context applicationContext = context.getApplicationContext();
        if ((applicationContext instanceof Application ? applicationContext.getApplicationInfo().targetSdkVersion : 0) < 31 || Build.VERSION.SDK_INT < 31) {
            return true;
        }
        return checkPermission(context, Permission.BLUETOOTH_SCAN);
    }

    public static boolean hasLocationAccess(Context context) {
        return Build.VERSION.SDK_INT >= 33 ? isLocationPermissionAvailable(context) : isLocationServiceEnable(context) && isLocationPermissionAvailable(context);
    }

    public static boolean isBleAvailable(Context context) {
        if (context == null || context.getPackageManager() == null) {
            return false;
        }
        try {
            if (context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
                return BluetoothAdapter.getDefaultAdapter().isEnabled();
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean isLocationPermissionAvailable(Context context) {
        try {
            return Build.VERSION.SDK_INT >= 33 ? context.checkSelfPermission(Permission.NEARBY_WIFI_DEVICES) == 0 : Build.VERSION.SDK_INT < 23 || context.checkSelfPermission(Permission.ACCESS_FINE_LOCATION) == 0 || context.checkSelfPermission(Permission.ACCESS_COARSE_LOCATION) == 0;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return true;
    }

    public static boolean isLocationPermissionsGranted(Context context) {
        return Build.VERSION.SDK_INT >= 33 ? (ContextCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) == 0) || ContextCompat.checkSelfPermission(context, Permission.NEARBY_WIFI_DEVICES) == 0 : ContextCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) == 0;
    }

    public static boolean isLocationServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        if (locationManager == null) {
            return false;
        }
        return locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network");
    }
}
