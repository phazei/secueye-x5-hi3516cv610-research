package com.aliyun.ams.emas.push;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.aliyun.ams.emas.push.data.NotificationDataManager;
import com.aliyun.ams.emas.push.notification.AgooCPushNotification;
import com.aliyun.ams.emas.push.notification.AgooMessageNotification;
import com.aliyun.ams.emas.push.notification.CPushMessage;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.JsonUtility;
import java.util.Map;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.common.Config;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
class AgooPushHandler {
    private static final String TAG = "AgooPushHandler";

    AgooPushHandler() {
    }

    public static void handle(Context context, Intent intent, IAgooPushConfig iAgooPushConfig, IAgooPushCallback iAgooPushCallback) throws JSONException {
        Map<String, String> map;
        try {
            String stringExtra = intent.getStringExtra("id");
            if (TextUtils.isEmpty(stringExtra)) {
                ALog.e(TAG, "handle message Null messageId!", new Object[0]);
                return;
            }
            String stringExtra2 = intent.getStringExtra("body");
            String stringExtra3 = intent.getStringExtra("task_id");
            String stringExtra4 = intent.getStringExtra("extData");
            String stringExtra5 = intent.getStringExtra(AgooConstants.MESSAGE_SOURCE);
            if (TextUtils.isEmpty(stringExtra2)) {
                ALog.e(TAG, "handle message json body is Empty!", new Object[0]);
                return;
            }
            try {
                map = JsonUtility.toMap(new JSONObject(stringExtra2));
            } catch (JSONException e) {
                ALog.e(TAG, "Parse json error:", e, new Object[0]);
                map = null;
            }
            try {
                int i = Integer.parseInt(map.get("type"));
                PushConfigHolder.importantLogger.d("handle message, messageId:" + stringExtra + ", type:" + i + ", msg receive:" + stringExtra2);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    PushConfigHolder.importantLogger.d(entry.getKey() + " --> " + entry.getValue());
                }
                String str = map.get("msg_id");
                map.put("task_id", stringExtra3);
                map.put("extData", stringExtra4);
                map.put(AgooConstants.MESSAGE_SOURCE, stringExtra5);
                map.put(AgooConstants.MESSAGE_BODY_MSG_ID_ALIYUN_FLAG, str);
                if (PushConfigHolder.isInDoNotDisturbTimeWindow()) {
                    PushConfigHolder.importantLogger.d("Push received in DoNotDisturb time window, ignored.");
                } else {
                    createAndShowNotification(context, iAgooPushConfig, iAgooPushCallback, stringExtra, map, i);
                }
            } catch (Throwable th) {
                ALog.e(TAG, "Wrong message Type Define!", th, new Object[0]);
            }
        } catch (Throwable th2) {
            ALog.e(TAG, "onHandleCallException", th2, new Object[0]);
        }
    }

    private static void createNotification(final Context context, final IAgooPushConfig iAgooPushConfig, final Map<String, String> map, final IOnNotificationCreated iOnNotificationCreated) {
        String str = map.get("image");
        String str2 = map.get("big_picture");
        final String str3 = map.get("group");
        final boolean zCheckNotificationShowInInnerGroup = iAgooPushConfig.checkNotificationShowInInnerGroup(map);
        if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) {
            iOnNotificationCreated.created(iAgooPushConfig.customNotificationUI(context, map), (!TextUtils.isEmpty(str3) || zCheckNotificationShowInInnerGroup) ? iAgooPushConfig.customSummaryNotification(context, map) : null);
        } else {
            final Handler handler = Looper.myLooper() != null ? new Handler(Looper.myLooper()) : null;
            ThreadPoolExecutorFactory.execute(new Runnable() { // from class: com.aliyun.ams.emas.push.AgooPushHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    final Notification notificationCustomNotificationUI = iAgooPushConfig.customNotificationUI(context, map);
                    final Notification notificationCustomSummaryNotification = (!TextUtils.isEmpty(str3) || zCheckNotificationShowInInnerGroup) ? iAgooPushConfig.customSummaryNotification(context, map) : null;
                    Handler handler2 = handler;
                    if (handler2 != null) {
                        handler2.post(new Runnable() { // from class: com.aliyun.ams.emas.push.AgooPushHandler.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                iOnNotificationCreated.created(notificationCustomNotificationUI, notificationCustomSummaryNotification);
                            }
                        });
                    } else {
                        iOnNotificationCreated.created(notificationCustomNotificationUI, notificationCustomSummaryNotification);
                    }
                }
            });
        }
    }

    private static void createAndShowNotification(final Context context, IAgooPushConfig iAgooPushConfig, final IAgooPushCallback iAgooPushCallback, String str, final Map<String, String> map, int i) {
        final AgooMessageNotification agooMessageNotification = new AgooMessageNotification();
        switch (i) {
            case 1:
                try {
                    String agooAppKey = Config.getAgooAppKey(context);
                    final AgooCPushNotification agooCPushNotificationConvertToNotice = agooMessageNotification.convertToNotice(context, map, agooAppKey, str);
                    if (agooCPushNotificationConvertToNotice != null) {
                        PushConfigHolder.reportPushArrive(context, agooCPushNotificationConvertToNotice.getMessageId(), i);
                        if (iAgooPushConfig.showNotificationNow(context, map)) {
                            String group = agooCPushNotificationConvertToNotice.getGroup();
                            if (!TextUtils.isEmpty(group)) {
                                NotificationDataManager.getInstance().addGroupNotification(group, agooCPushNotificationConvertToNotice);
                            }
                            createNotification(context, iAgooPushConfig, map, new IOnNotificationCreated() { // from class: com.aliyun.ams.emas.push.AgooPushHandler.2
                                @Override // com.aliyun.ams.emas.push.IOnNotificationCreated
                                public void created(Notification notification, Notification notification2) {
                                    String str2 = (String) map.get(AgooConstants.MESSAGE_BODY_EMAS_GROUP);
                                    if (!TextUtils.isEmpty(str2)) {
                                        agooCPushNotificationConvertToNotice.setEmasGroup(str2);
                                    }
                                    PushConfigHolder.importantLogger.d("push created notification" + agooCPushNotificationConvertToNotice.getTitle());
                                    agooMessageNotification.onNotification(context, notification, notification2, agooCPushNotificationConvertToNotice);
                                    PushConfigHolder.importantLogger.d("push onNotificationShow " + agooCPushNotificationConvertToNotice.getTitle());
                                    iAgooPushCallback.onNotificationShow(context, agooCPushNotificationConvertToNotice.getTitle(), agooCPushNotificationConvertToNotice.getSummary(), agooCPushNotificationConvertToNotice.getExtraMap());
                                }
                            });
                        } else {
                            PushConfigHolder.importantLogger.i("do not build notification as user request");
                            iAgooPushCallback.onNotificationReceivedWithoutShow(context, agooCPushNotificationConvertToNotice.getTitle(), agooCPushNotificationConvertToNotice.getSummary(), agooCPushNotificationConvertToNotice.getExtraMap(), agooCPushNotificationConvertToNotice.getOpenType(), agooCPushNotificationConvertToNotice.getOpenActivity(), agooCPushNotificationConvertToNotice.getOpenUrl());
                        }
                    } else {
                        ALog.e(TAG, "Notify title is null or server push data Error appId =  " + agooAppKey, new Object[0]);
                    }
                } catch (Throwable th) {
                    ALog.e(TAG, "Notify message error:", th, new Object[0]);
                }
                break;
            case 2:
                try {
                    CPushMessage cPushMessageConvertToMessage = agooMessageNotification.convertToMessage(context, map, Config.getAgooAppKey(context), str);
                    if (cPushMessageConvertToMessage != null) {
                        PushConfigHolder.reportPushArrive(context, cPushMessageConvertToMessage.getMessageId(), i);
                        try {
                            ALog.i(TAG, "messageId=" + cPushMessageConvertToMessage.getMessageId() + ";appId=" + cPushMessageConvertToMessage.getAppId() + ";messageType=msg", null, 1);
                        } catch (Throwable th2) {
                            ALog.e(TAG, "ut log error", th2, new Object[0]);
                        }
                        iAgooPushCallback.onMessageArrived(context, cPushMessageConvertToMessage);
                    }
                } catch (Throwable th3) {
                    ALog.e(TAG, "Custom message parse error:", th3, new Object[0]);
                    return;
                }
                break;
            default:
                ALog.e(TAG, "Wrong message Type Define!", new Object[0]);
                break;
        }
    }

    public static void handleNotificationClick(Context context, Intent intent, IAgooPushCallback iAgooPushCallback) {
        try {
            String stringExtra = intent.getStringExtra("messageId");
            String stringExtra2 = intent.getStringExtra("title");
            String stringExtra3 = intent.getStringExtra("summary");
            String stringExtra4 = intent.getStringExtra("extraMap");
            String stringExtra5 = intent.getStringExtra("group");
            int intExtra = intent.getIntExtra("notificationOpenType", 1);
            PushConfigHolder.importantLogger.d("notification opened " + stringExtra);
            if (!TextUtils.isEmpty(stringExtra5)) {
                NotificationDataManager.getInstance().clearGroup(stringExtra5);
            }
            iAgooPushCallback.onNotificationOpened(context, stringExtra2, stringExtra3, stringExtra4, intExtra);
        } catch (Throwable th) {
            ALog.e(TAG, "Handle notification open action failed.", th, new Object[0]);
        }
    }

    public static void handleNotificationDelete(Context context, Intent intent, IAgooPushCallback iAgooPushCallback) {
        try {
            String stringExtra = intent.getStringExtra("messageId");
            String stringExtra2 = intent.getStringExtra("title");
            String stringExtra3 = intent.getStringExtra("summary");
            String stringExtra4 = intent.getStringExtra("extraMap");
            String stringExtra5 = intent.getStringExtra("group");
            int intExtra = intent.getIntExtra("notificationOpenType", 1);
            PushConfigHolder.importantLogger.d("notification deleted " + stringExtra);
            if (!TextUtils.isEmpty(stringExtra5)) {
                NotificationDataManager.getInstance().clearGroup(stringExtra5);
            }
            iAgooPushCallback.onNotificationRemoved(context, stringExtra2, stringExtra3, stringExtra4, intExtra, stringExtra);
        } catch (Throwable th) {
            ALog.e(TAG, "Handle notification delete action failed.", th, new Object[0]);
        }
    }
}
