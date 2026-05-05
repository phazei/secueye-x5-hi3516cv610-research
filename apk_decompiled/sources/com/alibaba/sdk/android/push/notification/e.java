package com.alibaba.sdk.android.push.notification;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import java.util.List;
import java.util.Random;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Random f3164a;

    public static boolean a(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
            if (Build.VERSION.SDK_INT <= 16) {
                List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
                return runningTasks.isEmpty() || runningTasks.get(0).topActivity.getPackageName().equals(context.getPackageName());
            }
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(runningAppProcessInfo);
            return runningAppProcessInfo.importance == 100;
        } catch (Exception unused) {
            return false;
        }
    }
}
