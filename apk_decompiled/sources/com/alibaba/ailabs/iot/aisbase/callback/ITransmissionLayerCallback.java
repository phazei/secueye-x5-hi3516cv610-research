package com.alibaba.ailabs.iot.aisbase.callback;

import android.bluetooth.BluetoothDevice;

/* JADX INFO: loaded from: classes.dex */
public interface ITransmissionLayerCallback {
    void onA2DPConnectionStateUpdate(BluetoothDevice bluetoothDevice, int i);

    void onBindStateUpdate(BluetoothDevice bluetoothDevice, int i);

    void onConnectionStateUpdate(BluetoothDevice bluetoothDevice, int i);

    void onReceivedCommand(byte b2, byte[] bArr);

    void onReceivedStream(byte[] bArr);
}
