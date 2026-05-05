package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.da, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0424da implements IActionListener<BluetoothDevice> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ RunnableC0426ea f2567a;

    public C0424da(RunnableC0426ea runnableC0426ea) {
        this.f2567a = runnableC0426ea;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(BluetoothDevice bluetoothDevice) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
    }
}
