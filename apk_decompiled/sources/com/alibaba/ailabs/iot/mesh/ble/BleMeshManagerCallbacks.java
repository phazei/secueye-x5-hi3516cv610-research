package com.alibaba.ailabs.iot.mesh.ble;

import aisble.BleManagerCallbacks;
import android.bluetooth.BluetoothDevice;

/* JADX INFO: loaded from: classes.dex */
public interface BleMeshManagerCallbacks extends BleManagerCallbacks {
    void onDataReceived(BluetoothDevice bluetoothDevice, int i, byte[] bArr);

    void onDataSent(BluetoothDevice bluetoothDevice, int i, byte[] bArr);
}
