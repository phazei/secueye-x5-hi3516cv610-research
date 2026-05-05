package com.aliyun.ams.emas.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.taobao.accs.utl.ALog;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AgooMessageReceiver extends BroadcastReceiver implements IAgooPushCallback, IAgooPushConfig {
    public static final String EXTRA_MAP = "extraMap";
    public static final String MESSAGE_ID = "messageId";
    public static final String NOTIFICATION_GROUP = "group";
    public static final String NOTIFICATION_ID = "notificationId";
    public static final String NOTIFICATION_OPENED_ACTION = "com.alibaba.push2.action.NOTIFICATION_OPENED";
    public static final String NOTIFICATION_OPEN_TYPE = "notificationOpenType";
    public static final String NOTIFICATION_REMOVED_ACTION = "com.alibaba.push2.action.NOTIFICATION_REMOVED";
    public static final String SUMMARY = "summary";
    public static final String TAG = "MPS:AgooMessageReceiver";
    public static final String TITLE = "title";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        ALog.d(TAG, "AgooMessageReceiver onReceive begin...intent=" + intent, new Object[0]);
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        try {
            if (TextUtils.equals(AgooInnerService.AGOO_RECEIVE_ACTION, action)) {
                AgooPushHandler.handle(context, intent, this, this);
            } else if (TextUtils.equals(NOTIFICATION_OPENED_ACTION, action)) {
                AgooPushHandler.handleNotificationClick(context, intent, this);
            } else if (TextUtils.equals(NOTIFICATION_REMOVED_ACTION, action)) {
                AgooPushHandler.handleNotificationDelete(context, intent, this);
            } else if (TextUtils.equals(action, "android.net.conn.CONNECTIVITY_CHANGE") || TextUtils.equals(action, "android.intent.action.BOOT_COMPLETED") || TextUtils.equals(action, "android.intent.action.PACKAGE_ADDED") || TextUtils.equals(action, "android.intent.action.PACKAGE_REPLACED") || TextUtils.equals(action, "android.intent.action.USER_PRESENT") || TextUtils.equals(action, "android.intent.action.ACTION_POWER_CONNECTED") || TextUtils.equals(action, "android.intent.action.ACTION_POWER_DISCONNECTED")) {
                ALog.d(TAG, "USER ACTION: " + action, new Object[0]);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "handle action error:", th, new Object[0]);
        }
    }
}
