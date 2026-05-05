package com.alibaba.ailabs.iot.bluetoothlesdk;

import com.alibaba.ailabs.iot.aisbase.callback.IBluetoothDeviceWrapperCallback;

/* JADX INFO: loaded from: classes.dex */
public interface IGenieBLEDeviceCallback extends IBluetoothDeviceWrapperCallback {
    void onlineStateChanged(boolean z);
}
