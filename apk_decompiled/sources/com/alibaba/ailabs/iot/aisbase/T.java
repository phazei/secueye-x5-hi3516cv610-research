package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.basic.BasicProxy;

/* JADX INFO: compiled from: BasicProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class T implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2525a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ BasicProxy f2526b;

    public T(BasicProxy basicProxy, IActionListener iActionListener) {
        this.f2526b = basicProxy;
        this.f2525a = iActionListener;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        this.f2525a.onFailure(i, "");
    }
}
