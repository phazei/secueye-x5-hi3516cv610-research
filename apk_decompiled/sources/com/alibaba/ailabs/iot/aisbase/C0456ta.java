package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ta, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0456ta extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2679a;

    public C0456ta(OTAPluginProxy oTAPluginProxy) {
        this.f2679a = oTAPluginProxy;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        LogUtils.i(this.f2679a.f2633a, "onReceive action: " + intent.getAction());
        if ("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED".equals(intent.getAction())) {
            int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", Integer.MIN_VALUE);
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            if (bluetoothDevice == null || this.f2679a.t == null || this.f2679a.v == null || !bluetoothDevice.getAddress().equals(this.f2679a.v.getAddress()) || intExtra != 2) {
                return;
            }
            if (this.f2679a.P != null) {
                this.f2679a.f2636d.removeCallbacks(this.f2679a.P);
            }
            this.f2679a.t.connectDevice(this.f2679a.v, new C0454sa(this));
        }
    }
}
