package com.linkkit.tools.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import java.util.List;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes3.dex */
public class AppUtils {
    public static Context getContext() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Object objInvoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(cls, new Object[0]);
            return (Context) objInvoke.getClass().getMethod("getApplication", new Class[0]).invoke(objInvoke, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentThreadInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ThreadInfo[threadId=");
        stringBuffer.append(Thread.currentThread().getId());
        stringBuffer.append(": threadName=" + Thread.currentThread().getName());
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    private String getProcessName(Context context) {
        try {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME)).getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return null;
            }
            int iMyPid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == iMyPid) {
                    return runningAppProcessInfo.processName;
                }
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getApplicationContext().getPackageName(), 0)));
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String getVersionName(Context context) {
        try {
            return context.getApplicationContext().getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionName;
        } catch (Throwable unused) {
            return "";
        }
    }
}
