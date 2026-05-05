package com.alibaba.sdk.android.push.notification;

import android.R;
import android.app.Notification;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.alibaba.sdk.android.push.common.util.JSONUtils;
import com.aliyun.ams.emas.push.data.NotificationDataManager;
import com.aliyun.ams.emas.push.notification.AgooMessageNotification;
import java.util.Map;
import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static AmsLogger f3163a = AmsLogger.getLogger(AgooMessageNotification.TAG);

    private Uri a(Context context, b bVar) {
        StringBuilder sb;
        String packageName;
        Uri uri;
        Uri defaultUri = Uri.EMPTY;
        if (!TextUtils.isEmpty(bVar.a())) {
            String strA = bVar.a();
            if (strA.startsWith("android.resource://")) {
                uri = Uri.parse(strA);
            } else {
                if (strA.startsWith("/raw/")) {
                    sb = new StringBuilder();
                    sb.append("android.resource://");
                    packageName = context.getPackageName();
                } else {
                    sb = new StringBuilder();
                    sb.append("android.resource://");
                    sb.append(context.getPackageName());
                    packageName = "/raw/";
                }
                sb.append(packageName);
                sb.append(strA);
                uri = Uri.parse(sb.toString());
            }
            defaultUri = uri;
        } else if (com.alibaba.sdk.android.push.common.a.b.a() != null) {
            defaultUri = Uri.parse(com.alibaba.sdk.android.push.common.a.b.a());
        } else {
            int identifier = context.getResources().getIdentifier("alicloud_notification_sound", "raw", context.getPackageName());
            f3163a.d("sound resId:" + identifier);
            if (identifier != 0) {
                defaultUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + identifier);
                f3163a.d("sound resId:" + identifier + "  ;uri:" + defaultUri.toString());
            }
        }
        if (defaultUri == Uri.EMPTY) {
            defaultUri = RingtoneManager.getDefaultUri(2);
        }
        f3163a.d("soundUri:" + defaultUri.toString());
        return defaultUri;
    }

    private void a(Context context, b bVar, Notification notification) {
        String strA;
        StringBuilder sb;
        String packageName;
        Uri uri;
        long[] jArr = {100, 250, 100, 250, 100, 250};
        switch (bVar.d()) {
            case 0:
            default:
                return;
            case 1:
                notification.vibrate = jArr;
                return;
            case 2:
                break;
            case 3:
                notification.vibrate = jArr;
                break;
        }
        Uri defaultUri = Uri.EMPTY;
        if (TextUtils.isEmpty(bVar.a())) {
            if (com.alibaba.sdk.android.push.common.a.b.a() != null) {
                strA = com.alibaba.sdk.android.push.common.a.b.a();
            } else {
                int identifier = context.getResources().getIdentifier("alicloud_notification_sound", "raw", context.getPackageName());
                if (identifier != 0) {
                    strA = "android.resource://" + context.getPackageName() + "/" + identifier;
                }
            }
            defaultUri = Uri.parse(strA);
        } else {
            String strA2 = bVar.a();
            if (strA2.startsWith("android.resource://")) {
                uri = Uri.parse(strA2);
            } else {
                if (strA2.startsWith("/raw/")) {
                    sb = new StringBuilder();
                    sb.append("android.resource://");
                    packageName = context.getPackageName();
                } else {
                    sb = new StringBuilder();
                    sb.append("android.resource://");
                    sb.append(context.getPackageName());
                    packageName = "/raw/";
                }
                sb.append(packageName);
                sb.append(strA2);
                uri = Uri.parse(sb.toString());
            }
            defaultUri = uri;
        }
        if (defaultUri == Uri.EMPTY) {
            defaultUri = RingtoneManager.getDefaultUri(2);
        }
        notification.sound = defaultUri;
    }

    public static boolean a(Map<String, String> map) {
        int i;
        BasicCustomPushNotification basicCustomPushNotificationA;
        if (!map.containsKey(PushData.KEY_CUSTOM_NOTIFICATION_ID) || (i = Integer.parseInt(map.get(PushData.KEY_CUSTOM_NOTIFICATION_ID))) == 0 || (basicCustomPushNotificationA = CustomNotificationBuilder.getInstance().a(i)) == null) {
            return true;
        }
        return basicCustomPushNotificationA.isBuildWhenAppInForeground();
    }

    private void b(Context context, b bVar, Notification notification) {
        f3163a.d("custom notification feedback");
        long[] jArr = {100, 250, 100, 250, 100, 250};
        if (bVar.i() == 0) {
            return;
        }
        if (2 == bVar.i()) {
            notification.sound = a(context, bVar);
            return;
        }
        if (1 != bVar.i()) {
            if (3 != bVar.i()) {
                notification.defaults = -1;
                return;
            }
            notification.sound = a(context, bVar);
        }
        notification.vibrate = jArr;
    }

    public Notification a(Context context, b bVar, PushData pushData, NotificationConfigure notificationConfigure) {
        try {
            a aVar = new a();
            aVar.a(bVar.b());
            aVar.b(bVar.c());
            aVar.a(bVar.p());
            aVar.c(bVar.q());
            aVar.d(bVar.r());
            aVar.b(bVar.s());
            aVar.e(bVar.t());
            aVar.f(bVar.u());
            aVar.g(bVar.v());
            aVar.h(bVar.w());
            aVar.i(bVar.a());
            if (TextUtils.isEmpty(bVar.x())) {
                String strY = bVar.y();
                if (!TextUtils.isEmpty(strY)) {
                    aVar.k(strY);
                }
            } else {
                aVar.j(bVar.x());
            }
            Notification notificationB = aVar.b(context, pushData, notificationConfigure);
            if (notificationB == null) {
                notificationB = new Notification(R.drawable.stat_notify_chat, "", System.currentTimeMillis());
            }
            if (1 == bVar.g() || bVar.o()) {
                a(context, bVar, notificationB);
                if (bVar.e()) {
                    notificationB.flags |= 32;
                } else {
                    notificationB.flags |= 16;
                }
            } else {
                f3163a.d("custom notification option first");
                b(context, bVar, notificationB);
                notificationB.flags = bVar.h();
            }
            if (notificationConfigure != null) {
                notificationConfigure.configNotification(notificationB, pushData);
            }
            return notificationB;
        } catch (Throwable th) {
            f3163a.e("onNotification createSummaryNotification error", th);
            return null;
        }
    }

    public b a(Context context, Map<String, String> map) {
        String str = map.get("title");
        String str2 = map.get("content");
        if (StringUtil.isEmpty(str) || StringUtil.isEmpty(str2)) {
            f3163a.e("title or content of notify is empty: " + map);
            return null;
        }
        b bVar = new b();
        String strValueOf = map.get("remind");
        if (StringUtil.isEmpty(strValueOf)) {
            strValueOf = String.valueOf(3);
        }
        String str3 = map.get("music");
        String str4 = map.get("ext");
        String str5 = map.get("notification_channel");
        String str6 = map.get("style");
        String str7 = map.get("title");
        String str8 = map.get("big_body");
        String str9 = map.get("big_picture");
        String str10 = map.get("inbox_content");
        String str11 = map.get("group");
        String str12 = map.get(AgooConstants.MESSAGE_BODY_EMAS_GROUP);
        bVar.b(str);
        bVar.d(str2);
        bVar.c(str2);
        bVar.a(Integer.parseInt(strValueOf));
        bVar.f(str5);
        bVar.g(map.get("image"));
        bVar.l(str11);
        bVar.m(str12);
        if (!TextUtils.isEmpty(str6)) {
            try {
                bVar.k(Integer.parseInt(str6));
            } catch (Throwable unused) {
            }
        }
        bVar.h(str7);
        bVar.i(str8);
        bVar.j(str9);
        bVar.k(str10);
        if (StringUtil.isEmpty(str3)) {
            str3 = null;
        }
        bVar.a(str3);
        if (!StringUtil.isEmpty(str4)) {
            try {
                Map<String, String> map2 = JSONUtils.toMap(new JSONObject(str4));
                bVar.e(map2.containsKey("_ALIYUN_NOTIFICATION_PRIORITY_") ? map2.get("_ALIYUN_NOTIFICATION_PRIORITY_") : Build.VERSION.SDK_INT >= 16 ? String.valueOf(0) : String.valueOf(0));
                bVar.a(map2);
            } catch (JSONException e) {
                f3163a.e("Parse inner json(ext) error:", e);
            }
        }
        if (map.containsKey(PushData.KEY_CUSTOM_NOTIFICATION_ID)) {
            int i = Integer.parseInt(map.get(PushData.KEY_CUSTOM_NOTIFICATION_ID));
            if (i != 0) {
                BasicCustomPushNotification basicCustomPushNotificationA = CustomNotificationBuilder.getInstance().a(i);
                if (basicCustomPushNotificationA == null) {
                    f3163a.w("custom notification is null");
                } else {
                    bVar.c(basicCustomPushNotificationA.getNotificationType());
                    bVar.b(basicCustomPushNotificationA.getStatusBarDrawable());
                    bVar.e(basicCustomPushNotificationA.getRemindType());
                    bVar.d(basicCustomPushNotificationA.getNotificationFlags());
                    bVar.a(basicCustomPushNotificationA.isServerOptionFirst());
                    if (3 == basicCustomPushNotificationA.getNotificationType()) {
                        AdvancedCustomPushNotification advancedCustomPushNotification = (AdvancedCustomPushNotification) basicCustomPushNotificationA;
                        bVar.i(advancedCustomPushNotification.getContentView());
                        bVar.f(advancedCustomPushNotification.getNotificationView());
                        bVar.h(advancedCustomPushNotification.getTitleView());
                        bVar.g(advancedCustomPushNotification.getIconView());
                        bVar.j(advancedCustomPushNotification.getIcon());
                    }
                }
            } else {
                f3163a.d("default notification");
            }
        }
        return bVar;
    }

    public Notification b(Context context, b bVar, PushData pushData, NotificationConfigure notificationConfigure) {
        String strC;
        Notification notificationA;
        try {
            a aVar = new a();
            String strX = bVar.x();
            String strY = bVar.y();
            aVar.a(bVar.b());
            if (TextUtils.isEmpty(strX)) {
                if (!TextUtils.isEmpty(strY)) {
                    aVar.k(bVar.y());
                }
                strC = bVar.c();
            } else {
                aVar.j(bVar.x());
                int groupNotifyCount = NotificationDataManager.getInstance().getGroupNotifyCount(strX);
                strC = groupNotifyCount > 1 ? context.getString(com.alibaba.sdk.android.push.R.string.unread_notification, Integer.valueOf(groupNotifyCount)) : bVar.c();
            }
            aVar.b(strC);
            aVar.a(bVar.p());
            aVar.c(bVar.q());
            aVar.d(bVar.r());
            aVar.b(bVar.s());
            aVar.e(bVar.t());
            aVar.f(bVar.u());
            aVar.g(bVar.v());
            aVar.h(bVar.w());
            aVar.i(bVar.a());
            if (1 != bVar.g()) {
                f3163a.d("building customNotification");
                notificationA = CustomNotificationBuilder.getInstance().a(context, bVar, pushData, notificationConfigure);
                if (notificationA == null) {
                    f3163a.e("build custom notification failed, build default notification");
                    notificationA = aVar.a(context, pushData, notificationConfigure);
                }
            } else {
                notificationA = aVar.a(context, pushData, notificationConfigure);
            }
            if (notificationA == null) {
                notificationA = new Notification(R.drawable.stat_notify_chat, "", System.currentTimeMillis());
            }
            if (1 == bVar.g() || bVar.o()) {
                a(context, bVar, notificationA);
                if (bVar.e()) {
                    notificationA.flags |= 32;
                } else {
                    notificationA.flags |= 16;
                }
            } else {
                f3163a.d("custom notification option first");
                b(context, bVar, notificationA);
                notificationA.flags = bVar.h();
            }
            if (notificationConfigure != null) {
                notificationConfigure.configNotification(notificationA, pushData);
            }
            return notificationA;
        } catch (Throwable th) {
            f3163a.e("onNotification", th);
            return null;
        }
    }
}
