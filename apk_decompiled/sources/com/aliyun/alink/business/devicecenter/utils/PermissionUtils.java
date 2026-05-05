package com.aliyun.alink.business.devicecenter.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.aliyun.alink.linksdk.tools.ALog;
import com.hjq.permissions.Permission;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionUtils {
    public static boolean a(Context context) {
        return Build.VERSION.SDK_INT >= 33 ? ActivityCompat.checkSelfPermission(context, Permission.NEARBY_WIFI_DEVICES) == -1 : ActivityCompat.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION) == -1 || ActivityCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) == -1;
    }

    public static boolean checkBlue() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        return (defaultAdapter == null || 10 == defaultAdapter.getState()) ? false : true;
    }

    public static boolean checkLocationCompetence(Context context) {
        return Build.VERSION.SDK_INT < 23 ? context.getPackageManager().checkPermission(Permission.ACCESS_FINE_LOCATION, context.getPackageName()) == 0 : ContextCompat.checkSelfPermission(context, Permission.ACCESS_FINE_LOCATION) == 0;
    }

    public static boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            return !TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed"));
        }
        try {
            return Settings.Secure.getInt(context.getContentResolver(), "location_mode") != 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void openBlue(Context context) {
        if (a()) {
            return;
        }
        try {
            startSystemAction(new Intent("android.settings.BLUETOOTH_SETTINGS"), context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestLocationService(Context context) {
        Intent intent;
        if (a(context)) {
            intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + context.getPackageName()));
        } else {
            intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        }
        startSystemAction(intent, context);
    }

    public static void startSystemAction(Intent intent, Context context) {
        try {
            if (intent.resolveActivity(context.getApplicationContext().getPackageManager()) != null) {
                if (!(context instanceof Activity) && Build.VERSION.SDK_INT >= 28) {
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("not systemAction->");
            sb.append(intent.getAction());
            ALog.d("PermissionUtils", sb.toString());
        } catch (Exception e) {
            ALog.e("PermissionUtils", e.toString());
            e.printStackTrace();
        }
    }

    public static boolean a() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            return false;
        }
        try {
            if (defaultAdapter.isEnabled()) {
                return true;
            }
            return defaultAdapter.enable();
        } catch (Exception unused) {
            return false;
        }
    }
}
