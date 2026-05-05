package com.aliyun.ams.emas.push.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Build;

/* JADX INFO: loaded from: classes2.dex */
public abstract class CPushNotificationBuilder {
    protected String group;
    protected String notificationChannel;
    protected int priority;
    protected String summary;
    protected String title;

    public abstract Notification buildNotification(Context context);

    public CPushNotificationBuilder() {
        int i = Build.VERSION.SDK_INT;
        this.priority = 0;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String str) {
        this.summary = str;
    }

    public void setPriority(int i) {
        this.priority = i;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setNotificationChannel(String str) {
        this.notificationChannel = str;
    }

    public String getNotificationChannel() {
        return this.notificationChannel;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String str) {
        this.group = str;
    }
}
