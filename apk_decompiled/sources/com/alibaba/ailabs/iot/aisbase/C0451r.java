package com.alibaba.ailabs.iot.aisbase;

import aisscanner.ScanRecord;
import aisscanner.ScanResult;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanSettings;
import androidx.annotation.NonNull;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.r, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplOreo.java */
/* JADX INFO: loaded from: classes.dex */
@TargetApi(26)
public class C0451r extends C0449q {
    @Override // com.alibaba.ailabs.iot.aisbase.C0449q, com.alibaba.ailabs.iot.aisbase.C0447p
    @NonNull
    public ScanSettings a(@NonNull BluetoothAdapter bluetoothAdapter, @NonNull aisscanner.ScanSettings scanSettings) {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        if (bluetoothAdapter.isOffloadedScanBatchingSupported() && scanSettings.getUseHardwareBatchingIfSupported()) {
            builder.setReportDelay(scanSettings.getReportDelayMillis());
        }
        if (scanSettings.getUseHardwareCallbackTypesIfSupported()) {
            builder.setCallbackType(scanSettings.getCallbackType()).setMatchMode(scanSettings.getMatchMode()).setNumOfMatches(scanSettings.getNumOfMatches());
        }
        builder.setScanMode(scanSettings.getScanMode()).setLegacy(scanSettings.getLegacy()).setPhy(scanSettings.getPhy());
        return builder.build();
    }

    @Override // com.alibaba.ailabs.iot.aisbase.C0447p
    @NonNull
    public ScanResult a(@NonNull android.bluetooth.le.ScanResult scanResult) {
        return new ScanResult(scanResult.getDevice(), (scanResult.getDataStatus() << 5) | (scanResult.isLegacy() ? 16 : 0) | scanResult.isConnectable(), scanResult.getPrimaryPhy(), scanResult.getSecondaryPhy(), scanResult.getAdvertisingSid(), scanResult.getTxPower(), scanResult.getRssi(), scanResult.getPeriodicAdvertisingInterval(), ScanRecord.parseFromBytes(scanResult.getScanRecord() != null ? scanResult.getScanRecord().getBytes() : null), scanResult.getTimestampNanos());
    }
}
