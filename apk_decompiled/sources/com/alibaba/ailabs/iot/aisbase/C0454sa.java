package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.sa, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0454sa implements IActionListener<BluetoothDevice> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0456ta f2643a;

    public C0454sa(C0456ta c0456ta) {
        this.f2643a = c0456ta;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(BluetoothDevice bluetoothDevice) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
    }
}
