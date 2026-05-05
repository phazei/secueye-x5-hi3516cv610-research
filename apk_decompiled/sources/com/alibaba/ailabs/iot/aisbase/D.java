package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayerManagerBase;

/* JADX INFO: compiled from: TransmissionLayerManagerBase.java */
/* JADX INFO: loaded from: classes.dex */
public class D implements BluetoothProfile.ServiceListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ TransmissionLayerManagerBase f2470a;

    public D(TransmissionLayerManagerBase transmissionLayerManagerBase) {
        this.f2470a = transmissionLayerManagerBase;
    }

    @Override // android.bluetooth.BluetoothProfile.ServiceListener
    public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
        TransmissionLayerManagerBase transmissionLayerManagerBase = this.f2470a;
        transmissionLayerManagerBase.f = transmissionLayerManagerBase.getActiveMethodType((BluetoothA2dp) bluetoothProfile);
        BluetoothAdapter.getDefaultAdapter().closeProfileProxy(i, bluetoothProfile);
    }

    @Override // android.bluetooth.BluetoothProfile.ServiceListener
    public void onServiceDisconnected(int i) {
    }
}
