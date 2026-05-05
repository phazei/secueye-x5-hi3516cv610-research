package com.alibaba.sdk.android.push.notification;

import android.R;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import org.json.JSONArray;
import org.json.JSONException;

/* JADX INFO: loaded from: classes.dex */
public class a extends c {
    private static final AmsLogger n = AmsLogger.getLogger("MPS:BasicNotificationBuilder");

    private Bitmap a(Drawable drawable) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0207  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x02d7  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0326  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01ea  */
    /* JADX WARN: Type inference failed for: r0v29, types: [androidx.core.app.NotificationCompat$BigPictureStyle] */
    /* JADX WARN: Type inference failed for: r0v44, types: [androidx.core.app.NotificationCompat$Style] */
    /* JADX WARN: Type inference failed for: r0v46, types: [androidx.core.app.NotificationCompat$BigTextStyle] */
    /* JADX WARN: Type inference failed for: r0v62, types: [android.app.Notification$BigPictureStyle] */
    /* JADX WARN: Type inference failed for: r0v82, types: [android.app.Notification$Style] */
    /* JADX WARN: Type inference failed for: r0v84, types: [android.app.Notification$BigTextStyle] */
    /* JADX WARN: Type inference failed for: r10v2, types: [androidx.core.app.NotificationCompat$Builder] */
    /* JADX WARN: Type inference failed for: r10v3, types: [android.app.Notification$Builder] */
    /* JADX WARN: Type inference failed for: r19v0, types: [com.alibaba.sdk.android.push.notification.NotificationConfigure] */
    @Override // com.alibaba.sdk.android.push.notification.c
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.app.Notification a(android.content.Context r17, com.alibaba.sdk.android.push.notification.PushData r18, com.alibaba.sdk.android.push.notification.NotificationConfigure r19) {
        /*
            Method dump skipped, instruction units count: 814
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.push.notification.a.a(android.content.Context, com.alibaba.sdk.android.push.notification.PushData, com.alibaba.sdk.android.push.notification.NotificationConfigure):android.app.Notification");
    }

    @Override // com.alibaba.sdk.android.push.notification.c
    public Notification b(Context context, PushData pushData, NotificationConfigure notificationConfigure) {
        String strE = e();
        String strF = f();
        if (TextUtils.isEmpty(strE) && TextUtils.isEmpty(strF)) {
            n.d("body group and emas group all empty");
            return null;
        }
        int iC = com.alibaba.sdk.android.push.common.a.b.c() != 0 ? com.alibaba.sdk.android.push.common.a.b.c() : context.getResources().getIdentifier(CustomNotificationBuilder.NOTIFICATION_SMALL_ICON_FILE, CustomNotificationBuilder.NOTIFICATION_ICON_RES_TYPE, context.getPackageName());
        int i = R.drawable.stat_notify_chat;
        int i2 = 0;
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            n.e("Get system icon error, package name not found, ", e);
        }
        if (iC == 0) {
            iC = i;
        }
        if (Build.VERSION.SDK_INT < 16) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(iC);
            if (!TextUtils.isEmpty(strE)) {
                builder.setGroup(strE);
            } else if (!TextUtils.isEmpty(strF)) {
                builder.setGroup(strF);
            }
            builder.setGroupSummary(true);
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            if (!TextUtils.isEmpty(this.k)) {
                try {
                    JSONArray jSONArray = new JSONArray(this.k);
                    while (i2 < jSONArray.length()) {
                        inboxStyle.addLine(jSONArray.getString(i2));
                        i2++;
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
            builder.setStyle(inboxStyle);
            return builder.build();
        }
        Notification.Builder builder2 = new Notification.Builder(context);
        builder2.setSmallIcon(iC);
        if (Build.VERSION.SDK_INT >= 20) {
            if (!TextUtils.isEmpty(strE)) {
                builder2.setGroup(strE);
            } else if (!TextUtils.isEmpty(strF)) {
                builder2.setGroup(strF);
            }
            builder2.setGroupSummary(true);
        }
        Notification.InboxStyle inboxStyle2 = new Notification.InboxStyle();
        if (!TextUtils.isEmpty(this.k)) {
            try {
                JSONArray jSONArray2 = new JSONArray(this.k);
                while (i2 < jSONArray2.length()) {
                    inboxStyle2.addLine(jSONArray2.getString(i2));
                    i2++;
                }
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
        }
        builder2.setStyle(inboxStyle2);
        if (Build.VERSION.SDK_INT >= 26 && !TextUtils.isEmpty(d())) {
            builder2.setChannelId(d());
        }
        return builder2.build();
    }
}
