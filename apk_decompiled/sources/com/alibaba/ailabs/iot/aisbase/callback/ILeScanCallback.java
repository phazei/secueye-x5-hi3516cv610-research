package com.alibaba.ailabs.iot.aisbase.callback;

import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;

/* JADX INFO: loaded from: classes.dex */
public interface ILeScanCallback {
    void onAliBLEDeviceFound(BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype);

    void onStartScan();

    void onStopScan();
}
