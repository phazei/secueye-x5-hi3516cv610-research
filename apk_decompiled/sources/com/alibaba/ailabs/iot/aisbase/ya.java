package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;

/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class ya implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2697a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2698b;

    public ya(OTAPluginProxy oTAPluginProxy, IActionListener iActionListener) {
        this.f2698b = oTAPluginProxy;
        this.f2697a = iActionListener;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        IActionListener iActionListener = this.f2697a;
        if (iActionListener != null) {
            iActionListener.onFailure(i, "request failed " + i);
        }
    }
}
