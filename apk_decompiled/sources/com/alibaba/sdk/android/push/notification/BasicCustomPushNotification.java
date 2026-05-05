package com.alibaba.sdk.android.push.notification;

import android.R;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class BasicCustomPushNotification implements Serializable {
    public static final String CUSTOM_NOTIFICATION_TAG = "custom_notification_";
    public static final int NOTIFICATION_TYPE_CUSTOM_ADVANCED = 3;
    public static final int NOTIFICATION_TYPE_CUSTOM_BASIC = 2;
    public static final int NOTIFICATION_TYPE_DEFAULT = 1;
    public static final int REMIND_TYPE_SILENT = 0;
    public static final int REMIND_TYPE_SOUND = 2;
    public static final int REMIND_TYPE_VIBRATE = 1;
    public static final int REMIND_TYPE_VIBRATE_AND_SOUND = 3;
    protected boolean buildWhenAppInForeground;
    protected int notificationFlags;
    protected int notificationType;
    protected int remindType;
    protected boolean serverOptionFirst;
    protected int statusBarDrawable;

    public BasicCustomPushNotification() {
        this.buildWhenAppInForeground = true;
        this.statusBarDrawable = R.drawable.stat_notify_chat;
        this.remindType = 3;
        this.notificationFlags = 16;
        this.serverOptionFirst = false;
        this.notificationType = 2;
    }

    public BasicCustomPushNotification(int i, int i2, int i3) {
        this.buildWhenAppInForeground = true;
        this.statusBarDrawable = R.drawable.stat_notify_chat;
        this.remindType = 3;
        this.notificationFlags = 16;
        this.serverOptionFirst = false;
        this.notificationType = 2;
        this.statusBarDrawable = i;
        this.remindType = i3;
        this.notificationFlags = i2;
    }

    public int getNotificationFlags() {
        return this.notificationFlags;
    }

    public int getNotificationType() {
        return this.notificationType;
    }

    public int getRemindType() {
        return this.remindType;
    }

    public int getStatusBarDrawable() {
        return this.statusBarDrawable;
    }

    public boolean isBuildWhenAppInForeground() {
        return this.buildWhenAppInForeground;
    }

    public boolean isServerOptionFirst() {
        return this.serverOptionFirst;
    }

    public void setBuildWhenAppInForeground(boolean z) {
        this.buildWhenAppInForeground = z;
    }

    public void setNotificationFlags(int i) {
        this.notificationFlags = i;
    }

    public void setRemindType(int i) {
        if (i == 0 || 1 == i || 2 == i || 3 == i) {
            this.remindType = i;
        }
    }

    public void setServerOptionFirst(boolean z) {
        this.serverOptionFirst = z;
    }

    public void setStatusBarDrawable(int i) {
        this.statusBarDrawable = i;
    }

    public String toString() {
        return "type:" + this.notificationType + ", statusBarDrawable:" + this.statusBarDrawable + ", remindType:" + this.remindType + ", flags:" + this.notificationFlags + ", serverOptionFirst:" + this.serverOptionFirst + ", buildWhenAppInForeground:" + isBuildWhenAppInForeground();
    }
}
