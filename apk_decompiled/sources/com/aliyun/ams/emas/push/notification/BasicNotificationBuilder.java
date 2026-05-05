package com.aliyun.ams.emas.push.notification;

import android.R;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.taobao.accs.utl.ALog;
import java.util.Random;

/* JADX INFO: loaded from: classes2.dex */
public class BasicNotificationBuilder extends CPushNotificationBuilder {
    private static final String TAG = "BasicNotificationBuilder";
    private static Random sRandom;

    @Override // com.aliyun.ams.emas.push.notification.CPushNotificationBuilder
    public Notification buildNotification(Context context) {
        int i;
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            ALog.e(TAG, "Get system icon error, package name not found, ", e, new Object[0]);
            i = R.drawable.stat_notify_chat;
        }
        Bitmap bitmapDrawableToBitmap = drawableToBitmap(context.getResources().getDrawable(i));
        if (Build.VERSION.SDK_INT >= 16) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle(getTitle()).setContentText(getSummary()).setSmallIcon(i).setVibrate(new long[]{100, 250, 100, 250, 100, 250}).setSound(RingtoneManager.getDefaultUri(2)).setPriority(getPriority()).setAutoCancel(true).setLargeIcon(bitmapDrawableToBitmap).setWhen(System.currentTimeMillis()).setTicker("");
            if (Build.VERSION.SDK_INT >= 20 && !TextUtils.isEmpty(getGroup())) {
                builder.setGroup(getGroup());
            }
            if (Build.VERSION.SDK_INT >= 17) {
                builder.setShowWhen(true);
            }
            if (Build.VERSION.SDK_INT >= 26 && !TextUtils.isEmpty(getNotificationChannel())) {
                builder.setChannelId(getNotificationChannel());
            }
            if (Build.VERSION.SDK_INT >= 20) {
                if (shouldUseDifferentGroup()) {
                    builder.setGroupSummary(true);
                    if (sRandom == null) {
                        sRandom = new Random(System.currentTimeMillis());
                    }
                    builder.setGroup("group" + sRandom.nextInt());
                } else {
                    builder.setGroupSummary(false);
                    builder.setGroup("group");
                }
            }
            return builder.build();
        }
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context);
        builder2.setContentTitle(getTitle()).setContentText(getSummary()).setSmallIcon(i).setVibrate(new long[]{100, 250, 100, 250, 100, 250}).setSound(RingtoneManager.getDefaultUri(2)).setAutoCancel(true).setPriority(getPriority()).setLargeIcon(bitmapDrawableToBitmap).setTicker("").setWhen(System.currentTimeMillis()).setShowWhen(true);
        if (!TextUtils.isEmpty(getGroup())) {
            builder2.setGroup(getGroup());
        }
        return builder2.build();
    }

    private static boolean shouldUseDifferentGroup() {
        try {
            String brand = getBrand("ro.vivo.os.build.display.id");
            if (!Build.MANUFACTURER.equalsIgnoreCase("vivo") && !brand.startsWith("Funtouch")) {
                String brand2 = getBrand("ro.iqoo.os.build.display.id");
                if (brand2 != null) {
                    return !TextUtils.isEmpty(brand2.trim());
                }
                return false;
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static String getBrand(String str) {
        try {
            Class<?>[] clsArr = {String.class};
            Object[] objArr = {str};
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getDeclaredMethod(TmpConstant.PROPERTY_IDENTIFIER_GET, clsArr).invoke(cls, objArr);
        } catch (Throwable unused) {
            return "";
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }
}
