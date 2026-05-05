package com.aliyun.alink.linksdk.tmp.utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.hjq.permissions.Permission;

/* JADX INFO: loaded from: classes2.dex */
public class TmpPermissionUtils {
    private static final String TAG = "TmpPermissionUtils";

    public static boolean checkIsNeedRequestBleScanAndConnect() {
        if (!hasBleScanPermission(AApplication.getInstance().getApplicationContext())) {
            ALog.w(TAG, "startLoopScan hasBleScanPermission = false");
            return true;
        }
        if (!hasBleConnectPermission(AApplication.getInstance().getApplicationContext())) {
            ALog.w(TAG, "startLoopScan hasBleConnectPermission = false");
            return true;
        }
        ALog.w(TAG, "startLoopScan hasBleScanPermission and hasBleConnectPermission = true");
        return false;
    }

    public static boolean hasBleScanPermission(Context context) {
        if (context == null) {
            return false;
        }
        Context applicationContext = context.getApplicationContext();
        int i = applicationContext instanceof Application ? applicationContext.getApplicationInfo().targetSdkVersion : 0;
        ALog.d(TAG, "hasBleConnectPermission: var1=" + i + "；Build.VERSION.SDK_INT=" + Build.VERSION.SDK_INT);
        if (i < 31 || Build.VERSION.SDK_INT < 31) {
            return true;
        }
        return checkPermission(context, Permission.BLUETOOTH_SCAN);
    }

    public static boolean hasBleConnectPermission(Context context) {
        if (context == null) {
            return false;
        }
        Context applicationContext = context.getApplicationContext();
        int i = applicationContext instanceof Application ? applicationContext.getApplicationInfo().targetSdkVersion : 0;
        ALog.d(TAG, "hasBleConnectPermission: var1=" + i + "；Build.VERSION.SDK_INT=" + Build.VERSION.SDK_INT);
        if (i < 31 || Build.VERSION.SDK_INT < 31) {
            return true;
        }
        return checkPermission(context, Permission.BLUETOOTH_CONNECT);
    }

    public static boolean checkPermission(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 23) {
            int iCheckPermission = context.getPackageManager().checkPermission(str, context.getPackageName());
            ALog.d(TAG, "checkPermission: var2=" + iCheckPermission);
            return iCheckPermission == 0;
        }
        ALog.d(TAG, "checkPermission: checkSelfPermission =" + ContextCompat.checkSelfPermission(context, str));
        return ContextCompat.checkSelfPermission(context, str) == 0;
    }
}
