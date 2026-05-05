package receiver;

import activity.StartActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import tools.ActivityManager;
import tools.NotificationManagerEx;

/* JADX INFO: loaded from: classes4.dex */
public class NotificationReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (NotificationManagerEx.ACTION_NOTIFICATION_CLICK.equals(intent.getAction())) {
            Activity activity2 = ActivityManager.getInstance().getActivity();
            if (activity2 == null) {
                Intent intent2 = new Intent(context, (Class<?>) StartActivity.class);
                intent2.addFlags(268435456);
                context.startActivity(intent2);
            } else {
                Intent intent3 = new Intent(context, activity2.getClass());
                intent3.setFlags(805306368);
                context.startActivity(intent3);
            }
        }
    }
}
