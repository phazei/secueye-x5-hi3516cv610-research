package com.alibaba.ailabs.iot.aisbase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: compiled from: BLEScannerProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class Ea extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BLEScannerProxy f2472a;

    public Ea(BLEScannerProxy bLEScannerProxy) {
        this.f2472a = bLEScannerProxy;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 10);
        int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", 10);
        if (intExtra != 10) {
            if (intExtra == 12) {
                LogUtils.v(BLEScannerProxy.f2644a, "bluetooth enabled");
                return;
            } else if (intExtra != 13) {
                return;
            }
        }
        if (intExtra2 == 13 || intExtra2 == 10) {
            return;
        }
        LogUtils.v(BLEScannerProxy.f2644a, "bluetooth disabled");
        if (this.f2472a.h != null) {
            this.f2472a.stopDirectionalScan();
        }
        if (this.f2472a.g != null) {
            this.f2472a.stopScan();
        }
    }
}
