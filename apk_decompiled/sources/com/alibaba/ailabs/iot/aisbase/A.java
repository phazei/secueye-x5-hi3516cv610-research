package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayerManagerBase;

/* JADX INFO: compiled from: TransmissionLayerManagerBase.java */
/* JADX INFO: loaded from: classes.dex */
public class A implements IActionListener<BluetoothDevice> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ TransmissionLayerManagerBase f2457a;

    public A(TransmissionLayerManagerBase transmissionLayerManagerBase) {
        this.f2457a = transmissionLayerManagerBase;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(BluetoothDevice bluetoothDevice) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
    }
}
