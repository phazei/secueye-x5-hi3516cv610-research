package com.taobao.accs.net;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import java.util.Calendar;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class a extends f {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private PendingIntent f6360c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private AlarmManager f6361d;

    public a(Context context) {
        super(context);
        try {
            this.f6361d = (AlarmManager) this.f6376a.getSystemService(NotificationCompat.CATEGORY_ALARM);
        } catch (Throwable th) {
            ALog.e("AlarmHeartBeatMgr", "AlarmHeartBeatMgr", th, new Object[0]);
        }
    }

    @Override // com.taobao.accs.net.f
    protected void a(int i) {
        if (this.f6361d == null) {
            this.f6361d = (AlarmManager) this.f6376a.getSystemService(NotificationCompat.CATEGORY_ALARM);
        }
        if (this.f6361d == null) {
            ALog.e("AlarmHeartBeatMgr", "setInner null", new Object[0]);
            return;
        }
        if (this.f6360c == null) {
            Intent intent = new Intent();
            intent.setPackage(this.f6376a.getPackageName());
            intent.setAction(Constants.ACTION_COMMAND);
            intent.putExtra("command", 201);
            if (Build.VERSION.SDK_INT >= 23) {
                this.f6360c = PendingIntent.getBroadcast(this.f6376a, 0, intent, 67108864);
            } else {
                this.f6360c = PendingIntent.getBroadcast(this.f6376a, 0, intent, 0);
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(13, i);
        this.f6361d.set(0, calendar.getTimeInMillis(), this.f6360c);
    }
}
