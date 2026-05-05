package com.alibaba.ailabs.iot.gattlibrary.callback;

import android.bluetooth.BluetoothDevice;

/* JADX INFO: loaded from: classes.dex */
public interface IBLEConnectionStateListener {
    void onBLEConnectionStateChanged(BluetoothDevice bluetoothDevice, int i);
}
