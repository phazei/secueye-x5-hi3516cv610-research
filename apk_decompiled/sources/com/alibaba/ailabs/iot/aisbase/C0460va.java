package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.DataSentCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.va, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0460va implements DataSentCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2692a;

    public C0460va(OTAPluginProxy oTAPluginProxy) {
        this.f2692a = oTAPluginProxy;
    }

    @Override // aisble.callback.DataSentCallback
    public void onDataSent(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
    }
}
