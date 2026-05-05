package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ra, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0452ra implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2641a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2642b;

    public C0452ra(OTAPluginProxy oTAPluginProxy, IActionListener iActionListener) {
        this.f2642b = oTAPluginProxy;
        this.f2641a = iActionListener;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        IActionListener iActionListener = this.f2641a;
        if (iActionListener != null) {
            iActionListener.onFailure(i, "");
        }
    }
}
