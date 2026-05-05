package com.alibaba.sdk.android.push.common.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import org.android.agoo.common.AgooConstants;
import receiver.PushReceiver;

/* JADX INFO: loaded from: classes.dex */
public class AppInfoUtil {
    private static final String TAG = "MPS:AppInfoUtil";
    private static AmsLogger sLogger = AmsLogger.getLogger(TAG);

    public static String getAppVersionName(Context context) {
        if (context == null) {
            sLogger.e("Get app version name failed: context null");
            return null;
        }
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            sLogger.e("version name not found!", e);
            return null;
        }
    }

    public static boolean isComponentExists(Context context, String str, String str2) {
        if (context == null) {
            sLogger.e("Get component info failed: context null");
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context.getPackageName(), str);
        try {
            if (str2.equals("service")) {
                packageManager.getServiceInfo(componentName, 131584);
            } else if (str2.equals(AgooConstants.OPEN_ACTIIVTY_NAME)) {
                packageManager.getActivityInfo(componentName, 131584);
            } else {
                if (!str2.equals(PushReceiver.REC_TAG)) {
                    return false;
                }
                packageManager.getReceiverInfo(componentName, 131584);
            }
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            sLogger.e("component:" + str + " not found!");
            return false;
        }
    }

    public static boolean isPermissionGranted(Context context, String str) {
        if (context == null) {
            sLogger.e("Get permission info failed: context null");
            return false;
        }
        try {
            return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
        } catch (Throwable th) {
            sLogger.e("isPermissionGranted:Get permission info failed.", th);
            return false;
        }
    }
}
