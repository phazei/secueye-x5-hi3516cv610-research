package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;

/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class Aa implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2462a;

    public Aa(OTAPluginProxy oTAPluginProxy) {
        this.f2462a = oTAPluginProxy;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        this.f2462a.b(1, "Send request OTA failed, status: " + i);
    }
}
