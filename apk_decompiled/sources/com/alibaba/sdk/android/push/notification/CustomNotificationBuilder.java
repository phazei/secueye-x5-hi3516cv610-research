package com.alibaba.sdk.android.push.notification;

import android.R;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.CharEncoding;

/* JADX INFO: loaded from: classes.dex */
public class CustomNotificationBuilder {
    public static final String NOTIFICATION_ICON_RES_TYPE = "drawable";
    public static final String NOTIFICATION_LARGE_ICON_FILE = "alicloud_notification_largeicon";
    public static final String NOTIFICATION_SMALL_ICON_FILE = "alicloud_notification_smallicon";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static AmsLogger f3152a = AmsLogger.getLogger("MPS:CustomNotificationBuilder");

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static CustomNotificationBuilder f3153c = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<String, Object> f3154b;

    private CustomNotificationBuilder() {
        this.f3154b = null;
        if (this.f3154b == null) {
            this.f3154b = new HashMap();
        }
    }

    private int a(Context context, b bVar) {
        int i;
        int iF = bVar.f();
        if (iF != 0) {
            return iF;
        }
        int iC = com.alibaba.sdk.android.push.common.a.b.c() != 0 ? com.alibaba.sdk.android.push.common.a.b.c() : context.getResources().getIdentifier(NOTIFICATION_SMALL_ICON_FILE, NOTIFICATION_ICON_RES_TYPE, context.getPackageName());
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            f3152a.e("Get system icon error, package name not found, ", e);
            i = 17301623;
        }
        return iC == 0 ? i : iC;
    }

    private Bitmap a(Drawable drawable) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:108:0x027e  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0231  */
    /* JADX WARN: Type inference failed for: r0v20, types: [androidx.core.app.NotificationCompat$Style] */
    /* JADX WARN: Type inference failed for: r0v22, types: [androidx.core.app.NotificationCompat$BigTextStyle] */
    /* JADX WARN: Type inference failed for: r0v27, types: [android.app.Notification$BigPictureStyle] */
    /* JADX WARN: Type inference failed for: r0v46, types: [android.app.Notification$Style] */
    /* JADX WARN: Type inference failed for: r0v48, types: [android.app.Notification$BigTextStyle] */
    /* JADX WARN: Type inference failed for: r0v6, types: [androidx.core.app.NotificationCompat$BigPictureStyle] */
    /* JADX WARN: Type inference failed for: r12v2, types: [android.app.Notification$Builder] */
    /* JADX WARN: Type inference failed for: r21v0, types: [com.alibaba.sdk.android.push.notification.NotificationConfigure] */
    /* JADX WARN: Type inference failed for: r2v1, types: [androidx.core.app.NotificationCompat$Builder] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.app.Notification b(android.content.Context r18, com.alibaba.sdk.android.push.notification.b r19, com.alibaba.sdk.android.push.notification.PushData r20, com.alibaba.sdk.android.push.notification.NotificationConfigure r21) {
        /*
            Method dump skipped, instruction units count: 646
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.push.notification.CustomNotificationBuilder.b(android.content.Context, com.alibaba.sdk.android.push.notification.b, com.alibaba.sdk.android.push.notification.PushData, com.alibaba.sdk.android.push.notification.NotificationConfigure):android.app.Notification");
    }

    private Bitmap b(Context context, b bVar) {
        Bitmap bitmapA = !TextUtils.isEmpty(bVar.r()) ? com.alibaba.sdk.android.push.e.a.a(context, bVar.r(), "image") : null;
        if (bitmapA == null) {
            if (com.alibaba.sdk.android.push.common.a.b.b() != null) {
                bitmapA = com.alibaba.sdk.android.push.common.a.b.b();
            } else {
                int identifier = context.getResources().getIdentifier(NOTIFICATION_LARGE_ICON_FILE, NOTIFICATION_ICON_RES_TYPE, context.getPackageName());
                if (identifier != 0) {
                    bitmapA = a(context.getResources().getDrawable(identifier));
                }
            }
        }
        if (bitmapA != null) {
            return bitmapA;
        }
        int i = R.drawable.stat_notify_chat;
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            f3152a.e("Get system icon error, package name not found, ", e);
        }
        return a(context.getResources().getDrawable(i));
    }

    private Notification c(Context context, b bVar, PushData pushData, NotificationConfigure notificationConfigure) {
        int iK;
        int iN;
        StringBuilder sb;
        String packageName;
        Uri uri;
        StringBuilder sb2;
        String packageName2;
        Uri uri2;
        String strA = bVar.a();
        f3152a.d("building advanced custom notification");
        if (bVar.j() == 0) {
            return null;
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), bVar.j());
        remoteViews.setTextViewText(bVar.l(), bVar.b());
        remoteViews.setTextViewText(bVar.m(), bVar.c());
        if (bVar.n() != 0) {
            iK = bVar.k();
            iN = bVar.n();
        } else {
            iK = bVar.k();
            iN = R.drawable.stat_notify_chat;
        }
        remoteViews.setImageViewResource(iK, iN);
        String strX = bVar.x();
        String strY = bVar.y();
        if (Build.VERSION.SDK_INT < 16) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContent(remoteViews).setPriority(bVar.p()).setSmallIcon(a(context, bVar)).setTicker("").setShowWhen(true).setWhen(System.currentTimeMillis());
            if (!TextUtils.isEmpty(strX)) {
                builder.setGroup(strX);
            } else if (!TextUtils.isEmpty(strY)) {
                builder.setGroup(strY);
            }
            if (!TextUtils.isEmpty(strA)) {
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
                builder.setSound(uri);
            }
            if (notificationConfigure != null) {
                notificationConfigure.configBuilder(builder, pushData);
            }
            return builder.build();
        }
        Notification.Builder builder2 = new Notification.Builder(context);
        builder2.setContent(remoteViews).setPriority(bVar.p()).setSmallIcon(a(context, bVar)).setTicker("").setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= 20) {
            if (!TextUtils.isEmpty(strX)) {
                builder2.setGroup(strX);
            } else if (!TextUtils.isEmpty(strY)) {
                builder2.setGroup(strY);
            }
        }
        if (Build.VERSION.SDK_INT >= 17) {
            builder2.setShowWhen(true);
        }
        if (Build.VERSION.SDK_INT >= 26 && !TextUtils.isEmpty(bVar.q())) {
            builder2.setChannelId(bVar.q());
        }
        if (!TextUtils.isEmpty(strA)) {
            if (strA.startsWith("android.resource://")) {
                uri2 = Uri.parse(strA);
            } else {
                if (strA.startsWith("/raw/")) {
                    sb2 = new StringBuilder();
                    sb2.append("android.resource://");
                    packageName2 = context.getPackageName();
                } else {
                    sb2 = new StringBuilder();
                    sb2.append("android.resource://");
                    sb2.append(context.getPackageName());
                    packageName2 = "/raw/";
                }
                sb2.append(packageName2);
                sb2.append(strA);
                uri2 = Uri.parse(sb2.toString());
            }
            builder2.setSound(uri2);
        }
        if (notificationConfigure != null) {
            notificationConfigure.configBuilder(builder2, pushData);
        }
        return builder2.build();
    }

    public static CustomNotificationBuilder getInstance() {
        if (f3153c == null) {
            f3153c = new CustomNotificationBuilder();
        }
        return f3153c;
    }

    public Notification a(Context context, b bVar, PushData pushData, NotificationConfigure notificationConfigure) {
        if (2 == bVar.g()) {
            return b(context, bVar, pushData, notificationConfigure);
        }
        if (3 == bVar.g()) {
            return c(context, bVar, pushData, notificationConfigure);
        }
        return null;
    }

    public BasicCustomPushNotification a(int i) {
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;
        BasicCustomPushNotification basicCustomPushNotification;
        if (this.f3154b.containsKey(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i)) {
            f3152a.d("find custom notification from cache");
            return (BasicCustomPushNotification) this.f3154b.get(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i);
        }
        f3152a.d("do not find custom notification from cache, find it from SharedPreferences");
        BasicCustomPushNotification basicCustomPushNotification2 = null;
        String string = com.alibaba.sdk.android.ams.common.a.a.k().getString(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, null);
        try {
            if (string == null) {
                f3152a.e("no corresponding custom notificaiton");
                return null;
            }
            try {
                byteArrayInputStream = new ByteArrayInputStream(URLDecoder.decode(string, "UTF-8").getBytes(CharEncoding.ISO_8859_1));
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                basicCustomPushNotification = (BasicCustomPushNotification) objectInputStream.readObject();
            } catch (OptionalDataException e) {
                e = e;
            } catch (StreamCorruptedException e2) {
                e = e2;
            } catch (UnsupportedEncodingException e3) {
                e = e3;
            } catch (IOException e4) {
                e = e4;
            } catch (ClassNotFoundException e5) {
                e = e5;
            }
            try {
                objectInputStream.close();
                byteArrayInputStream.close();
                f3152a.d(basicCustomPushNotification.toString());
                if (basicCustomPushNotification != null) {
                    this.f3154b.put(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, basicCustomPushNotification);
                }
                return basicCustomPushNotification;
            } catch (OptionalDataException e6) {
                e = e6;
                basicCustomPushNotification2 = basicCustomPushNotification;
                f3152a.e("get custom notification failed", e);
                f3152a.d(basicCustomPushNotification2.toString());
                if (basicCustomPushNotification2 != null) {
                    this.f3154b.put(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, basicCustomPushNotification2);
                }
                return basicCustomPushNotification2;
            } catch (StreamCorruptedException e7) {
                e = e7;
                basicCustomPushNotification2 = basicCustomPushNotification;
                f3152a.e("get custom notification failed", e);
                f3152a.d(basicCustomPushNotification2.toString());
                if (basicCustomPushNotification2 != null) {
                    this.f3154b.put(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, basicCustomPushNotification2);
                }
                return basicCustomPushNotification2;
            } catch (UnsupportedEncodingException e8) {
                e = e8;
                basicCustomPushNotification2 = basicCustomPushNotification;
                f3152a.e("get custom notification failed", e);
                f3152a.d(basicCustomPushNotification2.toString());
                if (basicCustomPushNotification2 != null) {
                    this.f3154b.put(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, basicCustomPushNotification2);
                }
                return basicCustomPushNotification2;
            } catch (IOException e9) {
                e = e9;
                basicCustomPushNotification2 = basicCustomPushNotification;
                f3152a.e("get custom notification failed", e);
                f3152a.d(basicCustomPushNotification2.toString());
                if (basicCustomPushNotification2 != null) {
                    this.f3154b.put(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, basicCustomPushNotification2);
                }
                return basicCustomPushNotification2;
            } catch (ClassNotFoundException e10) {
                e = e10;
                basicCustomPushNotification2 = basicCustomPushNotification;
                f3152a.e("get custom notification failed", e);
                f3152a.d(basicCustomPushNotification2.toString());
                if (basicCustomPushNotification2 != null) {
                    this.f3154b.put(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, basicCustomPushNotification2);
                }
                return basicCustomPushNotification2;
            } catch (Throwable unused) {
                basicCustomPushNotification2 = basicCustomPushNotification;
                f3152a.d(basicCustomPushNotification2.toString());
                if (basicCustomPushNotification2 != null) {
                    this.f3154b.put(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, basicCustomPushNotification2);
                }
                return basicCustomPushNotification2;
            }
        } catch (Throwable unused2) {
        }
    }

    public boolean setCustomNotification(int i, BasicCustomPushNotification basicCustomPushNotification) {
        AmsLogger amsLogger;
        String str;
        boolean z = false;
        if (i <= 0) {
            amsLogger = f3152a;
            str = "custom notification id must be an integer greater than 0";
        } else {
            if (basicCustomPushNotification != null) {
                SharedPreferences sharedPreferencesK = com.alibaba.sdk.android.ams.common.a.a.k();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(basicCustomPushNotification);
                    String strEncode = URLEncoder.encode(byteArrayOutputStream.toString(CharEncoding.ISO_8859_1), "UTF-8");
                    objectOutputStream.close();
                    byteArrayOutputStream.close();
                    SharedPreferences.Editor editorEdit = sharedPreferencesK.edit();
                    editorEdit.putString(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, strEncode);
                    editorEdit.commit();
                    z = true;
                } catch (IOException e) {
                    f3152a.e("get custom notification failed", e);
                }
                if (z) {
                    if (this.f3154b.containsKey(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i)) {
                        this.f3154b.remove(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i);
                    }
                    f3152a.d("save the notification to cache");
                    this.f3154b.put(BasicCustomPushNotification.CUSTOM_NOTIFICATION_TAG + i, basicCustomPushNotification);
                }
                return z;
            }
            amsLogger = f3152a;
            str = "notification cannot be null";
        }
        amsLogger.e(str);
        return false;
    }
}
