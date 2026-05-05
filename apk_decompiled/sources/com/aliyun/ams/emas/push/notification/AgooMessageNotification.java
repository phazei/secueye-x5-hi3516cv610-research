package com.aliyun.ams.emas.push.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import com.aliyun.ams.emas.push.MsgService;
import com.aliyun.ams.emas.push.NotificationActivity;
import com.aliyun.ams.emas.push.PushConfigHolder;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.JsonUtility;
import java.util.Map;
import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class AgooMessageNotification {
    public static final String APP_ID = "appId";
    public static final String EXTRA_MAP = "extraMap";
    public static final String EXT_DATA = "extData";
    public static final String MSG_ID = "msgId";
    public static final String NOTIFICATION_GROUP = "group";
    public static final String NOTIFICATION_ID = "notificationId";
    public static final String NOTIFICATION_OPEN_TYPE = "notificationOpenType";
    public static final String SUMMARY = "summary";
    public static final String TAG = "MPS:MessageNotification";
    public static final String TASK_ID = "task_id";
    public static final String TITLE = "title";

    public CPushMessage convertToMessage(Context context, Map<String, String> map, String str, String str2) {
        String str3 = map.get("title");
        String str4 = map.get("content");
        String str5 = map.get("extData");
        if (TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) {
            ALog.e(TAG, "Message title or content is empty:" + map.toString(), new Object[0]);
            return null;
        }
        CPushMessage cPushMessage = new CPushMessage();
        cPushMessage.setMessageId(str2);
        cPushMessage.setAppId(str);
        cPushMessage.setTitle(str3);
        cPushMessage.setContent(str4);
        cPushMessage.setTraceInfo(str5);
        return cPushMessage;
    }

    public AgooCPushNotification convertToNotice(Context context, Map<String, String> map, String str, String str2) {
        int iCreateNotificationId;
        String str3;
        String str4 = map.get("title");
        String str5 = map.get("content");
        if (TextUtils.isEmpty(str4) || TextUtils.isEmpty(str5)) {
            ALog.e(TAG, "title or content of notify is empty: " + map, new Object[0]);
            return null;
        }
        AgooCPushNotification agooCPushNotification = new AgooCPushNotification();
        String strValueOf = map.get("open");
        if (TextUtils.isEmpty(strValueOf)) {
            strValueOf = String.valueOf(1);
        }
        String str6 = map.get("url");
        String str7 = map.get(AgooConstants.OPEN_ACTIIVTY_NAME);
        String str8 = map.get("ext");
        String str9 = map.get("task_id");
        String str10 = map.get("extData");
        String str11 = map.get("notification_channel");
        String str12 = map.get("notify_id");
        String str13 = map.get("group");
        if (!TextUtils.isEmpty(str13)) {
            iCreateNotificationId = str13.hashCode();
            str3 = str;
        } else if (!TextUtils.isEmpty(str12)) {
            iCreateNotificationId = Integer.parseInt(str12);
            str3 = str;
        } else {
            iCreateNotificationId = PushConfigHolder.createNotificationId();
            str3 = str;
        }
        agooCPushNotification.setAppId(str3);
        agooCPushNotification.setMessageId(str2);
        agooCPushNotification.setTaskId(str9);
        agooCPushNotification.setExtData(str10);
        agooCPushNotification.setSource(map.get(AgooConstants.MESSAGE_SOURCE));
        agooCPushNotification.setTitle(str4);
        agooCPushNotification.setSummary(str5);
        agooCPushNotification.setOpenType(Integer.parseInt(strValueOf));
        if (TextUtils.isEmpty(str6)) {
            str6 = null;
        }
        agooCPushNotification.setOpenUrl(str6);
        agooCPushNotification.setOpenActivity(TextUtils.isEmpty(str7) ? null : str7);
        agooCPushNotification.setNotificationId(iCreateNotificationId);
        agooCPushNotification.setNotificationChannel(str11);
        agooCPushNotification.setGroup(str13);
        if (!TextUtils.isEmpty(str8)) {
            try {
                Map<String, String> map2 = JsonUtility.toMap(new JSONObject(str8));
                map2.put("_ALIYUN_NOTIFICATION_ID_", String.valueOf(agooCPushNotification.getNotificationId()));
                if (map2.containsKey("_ALIYUN_NOTIFICATION_PRIORITY_")) {
                    agooCPushNotification.setPriority(map2.get("_ALIYUN_NOTIFICATION_PRIORITY_"));
                } else if (Build.VERSION.SDK_INT >= 16) {
                    agooCPushNotification.setPriority(String.valueOf(0));
                } else {
                    agooCPushNotification.setPriority(String.valueOf(0));
                }
                map2.put(AgooConstants.MESSAGE_BODY_MSG_ID_ALIYUN_FLAG, map.get(AgooConstants.MESSAGE_BODY_MSG_ID_ALIYUN_FLAG));
                agooCPushNotification.setExtraMap(map2);
            } catch (JSONException e) {
                ALog.e(TAG, "Parse inner json(ext) error:", e, new Object[0]);
            }
        }
        return agooCPushNotification;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0164 A[Catch: Throwable -> 0x017e, TryCatch #2 {Throwable -> 0x017e, blocks: (B:2:0x0000, B:4:0x000a, B:6:0x0038, B:7:0x0046, B:24:0x00e9, B:29:0x0141, B:31:0x0164, B:33:0x016a, B:34:0x0174, B:36:0x017a, B:28:0x0137, B:23:0x00e0, B:9:0x007e, B:10:0x0084, B:20:0x00c6, B:13:0x008b, B:18:0x00ae, B:19:0x00b8, B:15:0x00a1, B:25:0x00fd), top: B:45:0x0000, inners: #1, #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onNotification(android.content.Context r9, android.app.Notification r10, android.app.Notification r11, com.aliyun.ams.emas.push.notification.AgooCPushNotification r12) {
        /*
            Method dump skipped, instruction units count: 412
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.ams.emas.push.notification.AgooMessageNotification.onNotification(android.content.Context, android.app.Notification, android.app.Notification, com.aliyun.ams.emas.push.notification.AgooCPushNotification):void");
    }

    private PendingIntent buildDeletePendingIntent(Context context, AgooCPushNotification agooCPushNotification, int i) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), MsgService.class.getName());
        intent.setAction(PushConfigHolder.SERVICE_CONTAINER_ACTION);
        intent.putExtra("action_type", AgooConstants.NOTIFICATION_TYPE_DELETE);
        intent.putExtra("task_id", agooCPushNotification.getTaskId());
        intent.putExtra("extData", agooCPushNotification.getExtData());
        intent.putExtra("msgId", agooCPushNotification.getMessageId());
        intent.putExtra("title", agooCPushNotification.getTitle());
        intent.putExtra("summary", agooCPushNotification.getSummary());
        intent.putExtra("notificationOpenType", agooCPushNotification.getOpenType());
        intent.putExtra("notificationId", agooCPushNotification.getNotificationId());
        intent.putExtra("group", agooCPushNotification.getGroup());
        if (agooCPushNotification.getExtraMap() != null) {
            intent.putExtra("extraMap", new JSONObject(agooCPushNotification.getExtraMap()).toString());
        }
        ALog.d(TAG, "delete content messageId:" + agooCPushNotification.getMessageId(), new Object[0]);
        intent.putExtra(APP_ID, agooCPushNotification.getAppId());
        if (Build.VERSION.SDK_INT >= 23) {
            return PendingIntent.getService(context, i, intent, 201326592);
        }
        return PendingIntent.getService(context, i, intent, 134217728);
    }

    private PendingIntent buildContentPendingIntent(Context context, AgooCPushNotification agooCPushNotification, Intent intent, int i) {
        Intent intent2 = new Intent();
        if (Build.VERSION.SDK_INT > 30 || context.getApplicationInfo().targetSdkVersion > 30) {
            intent2.setClassName(context.getPackageName(), NotificationActivity.class.getName());
        } else {
            intent2.setClassName(context.getPackageName(), MsgService.class.getName());
        }
        intent2.setAction(PushConfigHolder.SERVICE_CONTAINER_ACTION);
        intent2.putExtra("action_type", AgooConstants.NOTIFICATION_TYPE_OPEN);
        intent2.putExtra("task_id", agooCPushNotification.getTaskId());
        intent2.putExtra("extData", agooCPushNotification.getExtData());
        String group = agooCPushNotification.getGroup();
        if (!TextUtils.isEmpty(group)) {
            intent2.putExtra("group", group);
        }
        intent.putExtra("title", agooCPushNotification.getTitle());
        intent.putExtra("summary", agooCPushNotification.getSummary());
        intent.putExtra("msgId", agooCPushNotification.getMessageId());
        intent.putExtra(APP_ID, agooCPushNotification.getAppId());
        intent.putExtra("notificationOpenType", agooCPushNotification.getOpenType());
        intent.putExtra("notificationId", agooCPushNotification.getNotificationId());
        if (!TextUtils.isEmpty(group)) {
            intent.putExtra("group", group);
        }
        intent2.putExtra("msgId", agooCPushNotification.getMessageId());
        if (agooCPushNotification.getExtraMap() != null) {
            intent.putExtra("extraMap", new JSONObject(agooCPushNotification.getExtraMap()).toString());
        }
        ALog.d(TAG, "build content messageId:" + agooCPushNotification.getMessageId(), new Object[0]);
        intent2.putExtra(AgooConstants.KEY_REAL_INTENT, intent);
        if (Build.VERSION.SDK_INT > 30 || context.getApplicationInfo().targetSdkVersion > 30) {
            return PendingIntent.getActivity(context, i, intent2, 201326592);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return PendingIntent.getService(context, i, intent2, 201326592);
        }
        return PendingIntent.getService(context, i, intent2, 134217728);
    }
}
