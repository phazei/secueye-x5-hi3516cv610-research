package tools;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;
import com.huawei.hms.push.constant.RemoteMessageConst;
import com.seculink.app.R;
import receiver.NotificationReceiver;
import sdk.DemoApplication;

/* JADX INFO: loaded from: classes4.dex */
public class NotificationManagerEx {
    public static final String ACTION_NOTIFICATION_CLICK = DemoApplication.getInstance().getPackageName() + ".notification.click";
    private static volatile NotificationManagerEx instance = null;
    private Application context;
    public NotificationManager nm;
    public int notifyId = 0;

    private NotificationManagerEx(Context context) {
        this.context = (Application) context.getApplicationContext();
        this.nm = (NotificationManager) this.context.getSystemService(RemoteMessageConst.NOTIFICATION);
    }

    public static synchronized NotificationManagerEx getInstance(Context context) {
        if (instance == null) {
            synchronized (NotificationManagerEx.class) {
                if (instance == null) {
                    instance = new NotificationManagerEx(context);
                }
            }
        }
        return instance;
    }

    public void alarm(String str, String str2) {
        this.notifyId++;
        notifyMessage(this.context, str, str2);
    }

    public void cancelAllMessages() {
        this.nm.cancelAll();
    }

    public void notifyMessage(Context context, String str, String str2) {
        Notification notificationBuild;
        Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) NotificationReceiver.class);
        intent.setAction(ACTION_NOTIFICATION_CLICK);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, (int) SystemClock.uptimeMillis(), intent, 268435456);
        if (Build.VERSION.SDK_INT >= 26) {
            String str3 = context.getPackageName() + "_alarm_channelId";
            NotificationChannel notificationChannel = new NotificationChannel(str3, context.getPackageName() + "_alarm_channelName", 4);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            this.nm.createNotificationChannel(notificationChannel);
            Notification.Builder smallIcon = new Notification.Builder(context, str3).setContentTitle(str).setContentText(str2).setSmallIcon(R.mipmap.app_launcher);
            smallIcon.setGroupSummary(false).setGroup("group");
            notificationBuild = smallIcon.build();
        } else {
            NotificationCompat.Builder smallIcon2 = new NotificationCompat.Builder(context).setContentTitle(str).setContentText(str2).setSmallIcon(R.mipmap.app_launcher);
            if (Build.VERSION.SDK_INT >= 24) {
                smallIcon2.setGroupSummary(false).setGroup("group");
            }
            notificationBuild = smallIcon2.build();
        }
        notificationBuild.defaults = 6;
        notificationBuild.flags |= 16;
        notificationBuild.tickerText = str2;
        notificationBuild.contentIntent = broadcast;
        this.nm.notify(this.notifyId, notificationBuild);
    }
}
