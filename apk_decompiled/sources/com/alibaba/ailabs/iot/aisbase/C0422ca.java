package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.channel.LayerState;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ca, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0422ca implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2555a;

    public C0422ca(OTAPluginProxy oTAPluginProxy) {
        this.f2555a = oTAPluginProxy;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        LogUtils.e(this.f2555a.f2633a, "Send OTA PDU failed: PDU index: " + this.f2555a.o);
        if (this.f2555a.t.getConnectionState() == LayerState.CONNECTED) {
            this.f2555a.f();
        } else {
            this.f2555a.b(0, "Bluetooth connection has been disconnected");
        }
    }
}
