package com.alibaba.ailabs.tg.utils;

import android.app.NotificationManager;
import android.content.Context;
import androidx.appcompat.app.NotificationCompat;
import com.huawei.hms.push.constant.RemoteMessageConst;

/* JADX INFO: loaded from: classes.dex */
public class NotificationUtils {
    public static void notification(Context context, int i, NotificationCompat.Builder builder) {
        notification(context, null, i, builder);
    }

    public static void notification(Context context, String str, int i, NotificationCompat.Builder builder) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(RemoteMessageConst.NOTIFICATION);
        if (notificationManager != null) {
            notificationManager.notify(i, builder.build());
        }
    }
}
