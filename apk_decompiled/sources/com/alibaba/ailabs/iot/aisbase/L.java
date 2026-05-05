package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;

/* JADX INFO: compiled from: AuthPluginBusinessProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class L implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2496a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ AuthPluginBusinessProxy f2497b;

    public L(AuthPluginBusinessProxy authPluginBusinessProxy, IActionListener iActionListener) {
        this.f2497b = authPluginBusinessProxy;
        this.f2496a = iActionListener;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        this.f2496a.onFailure(i, "");
    }
}
