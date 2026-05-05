package tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes4.dex */
public class FinishBReceiver extends BroadcastReceiver {

    /* JADX INFO: renamed from: activity, reason: collision with root package name */
    Activity f8105activity;
    String str;

    public FinishBReceiver(Activity activity2, String str) {
        this.f8105activity = activity2;
        this.str = str;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(this.str)) {
            this.f8105activity.finish();
        }
    }
}
