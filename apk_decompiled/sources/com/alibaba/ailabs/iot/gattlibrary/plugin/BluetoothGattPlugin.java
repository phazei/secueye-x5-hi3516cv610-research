package com.alibaba.ailabs.iot.gattlibrary.plugin;

import android.bluetooth.BluetoothGatt;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;

/* JADX INFO: loaded from: classes.dex */
public interface BluetoothGattPlugin extends IPlugin {
    boolean isCommandSupported(byte b2);

    boolean isRequiredServiceSupported(@NonNull BluetoothGatt bluetoothGatt);
}
