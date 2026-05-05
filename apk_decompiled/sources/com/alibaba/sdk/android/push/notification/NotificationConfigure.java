package com.alibaba.sdk.android.push.notification;

import android.app.Notification;
import androidx.core.app.NotificationCompat;

/* JADX INFO: loaded from: classes.dex */
public interface NotificationConfigure {
    void configBuilder(Notification.Builder builder, PushData pushData);

    void configBuilder(NotificationCompat.Builder builder, PushData pushData);

    void configNotification(Notification notification, PushData pushData);
}
