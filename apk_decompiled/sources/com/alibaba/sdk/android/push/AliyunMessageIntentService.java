package com.alibaba.sdk.android.push;

import android.app.Notification;
import android.content.Context;
import com.alibaba.sdk.android.push.b.b;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.alibaba.sdk.android.push.notification.NotificationConfigure;
import com.alibaba.sdk.android.push.notification.PushData;
import com.alibaba.sdk.android.push.notification.d;
import com.alibaba.sdk.android.push.notification.e;
import com.aliyun.ams.emas.push.AgooMessageIntentService;
import java.util.Map;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public abstract class AliyunMessageIntentService extends AgooMessageIntentService {
    private final d mMessageNotification = new d();

    @Override // com.aliyun.ams.emas.push.IAgooPushConfig
    public boolean checkNotificationShowInInnerGroup(Map<String, String> map) {
        boolean zC = b.a().c();
        if (zC) {
            map.put(AgooConstants.MESSAGE_BODY_EMAS_GROUP, AgooConstants.ACCS_PUSH_GROUP);
        }
        return zC;
    }

    public Notification customNotificationUI(Context context, PushData pushData) {
        return null;
    }

    @Override // com.aliyun.ams.emas.push.IAgooPushConfig
    public Notification customNotificationUI(Context context, Map<String, String> map) {
        PushData pushData = PushData.parse(context, map);
        NotificationConfigure notificationConfigureHookNotificationBuild = hookNotificationBuild();
        Notification notificationCustomNotificationUI = customNotificationUI(context, pushData);
        if (notificationCustomNotificationUI != null) {
            return notificationCustomNotificationUI;
        }
        return this.mMessageNotification.b(getApplicationContext(), this.mMessageNotification.a(getApplicationContext(), map), pushData, notificationConfigureHookNotificationBuild);
    }

    @Override // com.aliyun.ams.emas.push.IAgooPushConfig
    public Notification customSummaryNotification(Context context, Map<String, String> map) {
        PushData pushData = PushData.parse(context, map);
        NotificationConfigure notificationConfigureHookNotificationBuild = hookNotificationBuild();
        return this.mMessageNotification.a(context, this.mMessageNotification.a(context, map), pushData, notificationConfigureHookNotificationBuild);
    }

    public NotificationConfigure hookNotificationBuild() {
        return null;
    }

    protected abstract void onMessage(Context context, CPushMessage cPushMessage);

    @Override // com.aliyun.ams.emas.push.IAgooPushCallback
    public void onMessageArrived(Context context, com.aliyun.ams.emas.push.notification.CPushMessage cPushMessage) {
        onMessage(context, CPushMessage.from(cPushMessage));
    }

    protected abstract void onNotification(Context context, String str, String str2, Map<String, String> map);

    protected abstract void onNotificationClickedWithNoAction(Context context, String str, String str2, String str3);

    protected abstract void onNotificationOpened(Context context, String str, String str2, String str3);

    @Override // com.aliyun.ams.emas.push.IAgooPushCallback
    public void onNotificationOpened(Context context, String str, String str2, String str3, int i) {
        if (i == 4) {
            onNotificationClickedWithNoAction(context, str, str2, str3);
        } else {
            onNotificationOpened(context, str, str2, str3);
        }
    }

    protected abstract void onNotificationReceivedInApp(Context context, String str, String str2, Map<String, String> map, int i, String str3, String str4);

    @Override // com.aliyun.ams.emas.push.IAgooPushCallback
    public void onNotificationReceivedWithoutShow(Context context, String str, String str2, Map<String, String> map, int i, String str3, String str4) {
        onNotificationReceivedInApp(context, str, str2, map, i, str3, str4);
    }

    protected abstract void onNotificationRemoved(Context context, String str);

    @Override // com.aliyun.ams.emas.push.IAgooPushCallback
    public void onNotificationRemoved(Context context, String str, String str2, String str3, int i, String str4) {
        onNotificationRemoved(context, str4);
    }

    @Override // com.aliyun.ams.emas.push.IAgooPushCallback
    public void onNotificationShow(Context context, String str, String str2, Map<String, String> map) {
        onNotification(context, str, str2, map);
    }

    @Override // com.aliyun.ams.emas.push.IAgooPushConfig
    public boolean showNotificationNow(Context context, Map<String, String> map) {
        return d.a(map) || !e.a(context);
    }
}
