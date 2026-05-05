package com.alibaba.ailabs.iot.aisbase;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanSettings;
import androidx.annotation.NonNull;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.q, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplMarshmallow.java */
/* JADX INFO: loaded from: classes.dex */
@TargetApi(23)
public class C0449q extends C0447p {
    @Override // com.alibaba.ailabs.iot.aisbase.C0447p
    @NonNull
    public ScanSettings a(@NonNull BluetoothAdapter bluetoothAdapter, @NonNull aisscanner.ScanSettings scanSettings) {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        if (bluetoothAdapter.isOffloadedScanBatchingSupported() && scanSettings.getUseHardwareBatchingIfSupported()) {
            builder.setReportDelay(scanSettings.getReportDelayMillis());
        }
        if (scanSettings.getUseHardwareCallbackTypesIfSupported()) {
            builder.setCallbackType(scanSettings.getCallbackType()).setMatchMode(scanSettings.getMatchMode()).setNumOfMatches(scanSettings.getNumOfMatches());
        }
        builder.setScanMode(scanSettings.getScanMode());
        return builder.build();
    }
}
