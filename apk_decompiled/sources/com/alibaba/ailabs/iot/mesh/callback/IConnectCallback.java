package com.alibaba.ailabs.iot.mesh.callback;

import android.bluetooth.BluetoothDevice;

/* JADX INFO: loaded from: classes.dex */
public interface IConnectCallback {
    void onConnected(BluetoothDevice bluetoothDevice);

    void onFailure(BluetoothDevice bluetoothDevice, int i, String str);

    void onReady(BluetoothDevice bluetoothDevice);
}
