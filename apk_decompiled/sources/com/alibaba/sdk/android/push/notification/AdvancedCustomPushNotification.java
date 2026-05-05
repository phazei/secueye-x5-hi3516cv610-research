package com.alibaba.sdk.android.push.notification;

/* JADX INFO: loaded from: classes.dex */
public class AdvancedCustomPushNotification extends BasicCustomPushNotification {
    private int contentView;
    private int icon = 0;
    private int iconView;
    private int notificationView;
    private int titleView;

    public AdvancedCustomPushNotification(int i, int i2, int i3, int i4) {
        this.notificationView = 0;
        this.iconView = 0;
        this.titleView = 0;
        this.contentView = 0;
        this.notificationType = 3;
        this.notificationView = i;
        this.titleView = i3;
        this.iconView = i2;
        this.contentView = i4;
    }

    public int getContentView() {
        return this.contentView;
    }

    public int getIcon() {
        return this.icon;
    }

    public int getIconView() {
        return this.iconView;
    }

    public int getNotificationView() {
        return this.notificationView;
    }

    public int getTitleView() {
        return this.titleView;
    }

    public void setIcon(int i) {
        this.icon = i;
    }

    @Override // com.alibaba.sdk.android.push.notification.BasicCustomPushNotification
    public String toString() {
        return super.toString() + ", notificationView:" + this.notificationView + ", titleView:" + this.titleView + ", iconView:" + this.titleView + ", contentView:" + this.contentView;
    }
}
