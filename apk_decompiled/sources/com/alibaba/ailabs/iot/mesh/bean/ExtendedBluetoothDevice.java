package com.alibaba.ailabs.iot.mesh.bean;

import a.a.a.a.b.m.a;
import aisscanner.ScanRecord;
import aisscanner.ScanResult;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import datasource.bean.ConfigurationData;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class ExtendedBluetoothDevice implements Parcelable {
    public static final Parcelable.Creator<ExtendedBluetoothDevice> CREATOR = new Parcelable.Creator<ExtendedBluetoothDevice>() { // from class: com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExtendedBluetoothDevice createFromParcel(Parcel parcel) {
            return new ExtendedBluetoothDevice(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExtendedBluetoothDevice[] newArray(int i) {
            return new ExtendedBluetoothDevice[i];
        }
    };
    public static final String TAG = "ExtendedBluetoothDevice";
    public static Method parseFromBytesMethod;
    public ConfigurationData configurationInfo;
    public final BluetoothDevice device;
    public String name;
    public String pk;
    public int rssi;
    public ScanRecord scanRecord;

    public static ScanRecord parseScanRecordFromBytes(byte[] bArr) {
        try {
            if (parseFromBytesMethod == null) {
                parseFromBytesMethod = Class.forName(ScanRecord.class.getName()).getDeclaredMethod("parseFromBytes", byte[].class);
                parseFromBytesMethod.setAccessible(true);
            }
            return (ScanRecord) parseFromBytesMethod.invoke(null, bArr);
        } catch (Exception e) {
            a.b(TAG, "parseScanRecordFromBytes: " + e.getMessage());
            return null;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return obj instanceof ExtendedBluetoothDevice ? this.device.getAddress().equals(((ExtendedBluetoothDevice) obj).device.getAddress()) : super.equals(obj);
    }

    public String getAddress() {
        return this.device.getAddress();
    }

    public ConfigurationData getConfigurationInfo() {
        return this.configurationInfo;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public String getName() {
        return this.name;
    }

    public String getPk() {
        return this.pk;
    }

    public int getRssi() {
        return this.rssi;
    }

    public ScanRecord getScanRecord() {
        return this.scanRecord;
    }

    public boolean matches(ScanResult scanResult) {
        return this.device.getAddress().equals(scanResult.getDevice().getAddress());
    }

    public void setConfigurationInfo(ConfigurationData configurationData) {
        this.configurationInfo = configurationData;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPk(String str) {
        this.pk = str;
    }

    public void setRssi(int i) {
        this.rssi = i;
    }

    public void setScanRecord(ScanRecord scanRecord) {
        this.scanRecord = scanRecord;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.device, i);
        parcel.writeString(this.name);
        parcel.writeInt(this.rssi);
        if (this.scanRecord == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            parcel.writeByteArray(this.scanRecord.getBytes());
        }
    }

    public ExtendedBluetoothDevice(ScanResult scanResult) {
        this.device = scanResult.getDevice();
        this.rssi = scanResult.getRssi();
        this.scanRecord = scanResult.getScanRecord();
        ScanRecord scanRecord = this.scanRecord;
        if (scanRecord != null) {
            this.name = scanRecord.getDeviceName();
        }
    }

    public ExtendedBluetoothDevice(Parcel parcel) {
        this.device = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
        this.name = parcel.readString();
        this.rssi = parcel.readInt();
        if (parcel.readInt() == 1) {
            this.scanRecord = parseScanRecordFromBytes(parcel.createByteArray());
        }
    }
}
