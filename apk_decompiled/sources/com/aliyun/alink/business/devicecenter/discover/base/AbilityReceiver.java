package com.aliyun.alink.business.devicecenter.discover.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbilityReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f3586a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public BroadcastReceiver f3587b = new BroadcastReceiver() { // from class: com.aliyun.alink.business.devicecenter.discover.base.AbilityReceiver.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
            AbilityReceiver.this.onNotify(intent);
        }
    };

    public AbilityReceiver(Context context) {
        this.f3586a = null;
        this.f3586a = context.getApplicationContext();
    }

    public abstract void onNotify(Intent intent);

    public boolean register(String... strArr) {
        if (this.f3586a == null || strArr == null || strArr.length < 1) {
            return false;
        }
        IntentFilter intentFilter = new IntentFilter();
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            if (!TextUtils.isEmpty(strArr[i])) {
                intentFilter.addAction(strArr[i]);
            }
        }
        LocalBroadcastManager.getInstance(this.f3586a).registerReceiver(this.f3587b, intentFilter);
        return true;
    }

    public void unregister() {
        Context context = this.f3586a;
        if (context == null) {
            return;
        }
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this.f3587b);
    }
}
