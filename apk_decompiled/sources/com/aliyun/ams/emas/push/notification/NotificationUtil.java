package com.aliyun.ams.emas.push.notification;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import java.util.List;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationUtil {
    public static boolean isApplicationForeground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
            if (Build.VERSION.SDK_INT > 16) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
                ActivityManager.getMyMemoryState(runningAppProcessInfo);
                return runningAppProcessInfo.importance == 100;
            }
            List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
            if (runningTasks.isEmpty()) {
                return true;
            }
            return runningTasks.get(0).topActivity.getPackageName().equals(context.getPackageName());
        } catch (Exception unused) {
            return false;
        }
    }
}
