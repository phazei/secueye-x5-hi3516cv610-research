package com.alibaba.ailabs.iot.mesh.scan;

import aisscanner.ScanResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Scanner {
    public boolean mBluetoothEnabled;
    public boolean mStartScanning;
    public boolean mStopScanning;
    public Integer mUpdatedDeviceIndex;
    public final List<ExtendedBluetoothDevice> mDevices = new ArrayList();
    public boolean mScanningStarted = false;
    public boolean mLocationEnabled = false;

    public Scanner(boolean z) {
        this.mBluetoothEnabled = z;
    }

    @Nullable
    private Integer getUpdatedDeviceIndex() {
        Integer num = this.mUpdatedDeviceIndex;
        this.mUpdatedDeviceIndex = null;
        return num;
    }

    private int indexOf(ScanResult scanResult) {
        Iterator<ExtendedBluetoothDevice> it = this.mDevices.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (it.next().matches(scanResult)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void bluetoothDisabled() {
        this.mBluetoothEnabled = false;
        this.mUpdatedDeviceIndex = null;
        this.mDevices.clear();
    }

    public void bluetoothEnabled() {
        this.mBluetoothEnabled = true;
    }

    public void deviceDiscovered(ScanResult scanResult) {
        ExtendedBluetoothDevice extendedBluetoothDevice;
        int iIndexOf = indexOf(scanResult);
        if (iIndexOf == -1) {
            extendedBluetoothDevice = new ExtendedBluetoothDevice(scanResult);
            this.mDevices.add(extendedBluetoothDevice);
            this.mUpdatedDeviceIndex = null;
        } else {
            ExtendedBluetoothDevice extendedBluetoothDevice2 = this.mDevices.get(iIndexOf);
            this.mUpdatedDeviceIndex = Integer.valueOf(iIndexOf);
            extendedBluetoothDevice = extendedBluetoothDevice2;
        }
        extendedBluetoothDevice.setRssi(scanResult.getRssi());
        extendedBluetoothDevice.setScanRecord(scanResult.getScanRecord());
        if (scanResult.getScanRecord() != null) {
            extendedBluetoothDevice.setName(scanResult.getScanRecord().getDeviceName());
        }
    }

    @NonNull
    public List<ExtendedBluetoothDevice> getDevices() {
        return this.mDevices;
    }

    public boolean isBluetoothEnabled() {
        return this.mBluetoothEnabled;
    }

    public boolean isEmpty() {
        return this.mDevices.isEmpty();
    }

    public boolean isLocationEnabled() {
        return this.mLocationEnabled;
    }

    public boolean isScanning() {
        return this.mScanningStarted;
    }

    public boolean isStartScanRequested() {
        return this.mStartScanning;
    }

    public boolean isStopScanRequested() {
        return this.mStopScanning;
    }

    public void scanningStarted() {
        this.mScanningStarted = true;
    }

    public void scanningStopped() {
        this.mScanningStarted = false;
    }

    public void setLocationEnabled(boolean z) {
        this.mLocationEnabled = z;
    }

    public void startScanning() {
        this.mDevices.clear();
        this.mStopScanning = false;
        this.mStartScanning = true;
    }

    public void stopScanning() {
        this.mStopScanning = true;
        this.mStartScanning = false;
    }
}
