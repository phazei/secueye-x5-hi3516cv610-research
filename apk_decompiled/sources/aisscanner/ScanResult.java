package aisscanner;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.ailabs.iot.aisbase.C0457u;
import com.alibaba.ailabs.iot.aisbase.C0461w;

/* JADX INFO: loaded from: classes.dex */
public final class ScanResult implements Parcelable {
    public static final Parcelable.Creator<ScanResult> CREATOR = new C0461w();
    public static final int DATA_COMPLETE = 0;
    public static final int DATA_TRUNCATED = 2;
    public static final int ET_CONNECTABLE_MASK = 1;
    public static final int ET_LEGACY_MASK = 16;
    public static final int PERIODIC_INTERVAL_NOT_PRESENT = 0;
    public static final int PHY_UNUSED = 0;
    public static final int SID_NOT_PRESENT = 255;
    public static final int TX_POWER_NOT_PRESENT = 127;
    public int advertisingSid;

    @NonNull
    public BluetoothDevice device;
    public int eventType;
    public int periodicAdvertisingInterval;
    public int primaryPhy;
    public int rssi;

    @Nullable
    public ScanRecord scanRecord;
    public int secondaryPhy;
    public long timestampNanos;
    public int txPower;

    public /* synthetic */ ScanResult(Parcel parcel, C0461w c0461w) {
        this(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.device = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
        if (parcel.readInt() == 1) {
            this.scanRecord = ScanRecord.parseFromBytes(parcel.createByteArray());
        }
        this.rssi = parcel.readInt();
        this.timestampNanos = parcel.readLong();
        this.eventType = parcel.readInt();
        this.primaryPhy = parcel.readInt();
        this.secondaryPhy = parcel.readInt();
        this.advertisingSid = parcel.readInt();
        this.txPower = parcel.readInt();
        this.periodicAdvertisingInterval = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ScanResult.class != obj.getClass()) {
            return false;
        }
        ScanResult scanResult = (ScanResult) obj;
        return C0457u.b(this.device, scanResult.device) && this.rssi == scanResult.rssi && C0457u.b(this.scanRecord, scanResult.scanRecord) && this.timestampNanos == scanResult.timestampNanos && this.eventType == scanResult.eventType && this.primaryPhy == scanResult.primaryPhy && this.secondaryPhy == scanResult.secondaryPhy && this.advertisingSid == scanResult.advertisingSid && this.txPower == scanResult.txPower && this.periodicAdvertisingInterval == scanResult.periodicAdvertisingInterval;
    }

    public int getAdvertisingSid() {
        return this.advertisingSid;
    }

    public int getDataStatus() {
        return (this.eventType >> 5) & 3;
    }

    @NonNull
    public BluetoothDevice getDevice() {
        return this.device;
    }

    public int getPeriodicAdvertisingInterval() {
        return this.periodicAdvertisingInterval;
    }

    public int getPrimaryPhy() {
        return this.primaryPhy;
    }

    public int getRssi() {
        return this.rssi;
    }

    @Nullable
    public ScanRecord getScanRecord() {
        return this.scanRecord;
    }

    public int getSecondaryPhy() {
        return this.secondaryPhy;
    }

    public long getTimestampNanos() {
        return this.timestampNanos;
    }

    public int getTxPower() {
        return this.txPower;
    }

    public int hashCode() {
        return C0457u.a(this.device, Integer.valueOf(this.rssi), this.scanRecord, Long.valueOf(this.timestampNanos), Integer.valueOf(this.eventType), Integer.valueOf(this.primaryPhy), Integer.valueOf(this.secondaryPhy), Integer.valueOf(this.advertisingSid), Integer.valueOf(this.txPower), Integer.valueOf(this.periodicAdvertisingInterval));
    }

    public boolean isConnectable() {
        return (this.eventType & 1) != 0;
    }

    public boolean isLegacy() {
        return (this.eventType & 16) != 0;
    }

    public String toString() {
        return "ScanResult{device=" + this.device + ", scanRecord=" + C0457u.a(this.scanRecord) + ", rssi=" + this.rssi + ", timestampNanos=" + this.timestampNanos + ", eventType=" + this.eventType + ", primaryPhy=" + this.primaryPhy + ", secondaryPhy=" + this.secondaryPhy + ", advertisingSid=" + this.advertisingSid + ", txPower=" + this.txPower + ", periodicAdvertisingInterval=" + this.periodicAdvertisingInterval + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        this.device.writeToParcel(parcel, i);
        if (this.scanRecord != null) {
            parcel.writeInt(1);
            parcel.writeByteArray(this.scanRecord.getBytes());
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.rssi);
        parcel.writeLong(this.timestampNanos);
        parcel.writeInt(this.eventType);
        parcel.writeInt(this.primaryPhy);
        parcel.writeInt(this.secondaryPhy);
        parcel.writeInt(this.advertisingSid);
        parcel.writeInt(this.txPower);
        parcel.writeInt(this.periodicAdvertisingInterval);
    }

    public ScanResult(@NonNull BluetoothDevice bluetoothDevice, @Nullable ScanRecord scanRecord, int i, long j) {
        this.device = bluetoothDevice;
        this.scanRecord = scanRecord;
        this.rssi = i;
        this.timestampNanos = j;
        this.eventType = 17;
        this.primaryPhy = 1;
        this.secondaryPhy = 0;
        this.advertisingSid = 255;
        this.txPower = 127;
        this.periodicAdvertisingInterval = 0;
    }

    public ScanResult(@NonNull BluetoothDevice bluetoothDevice, int i, int i2, int i3, int i4, int i5, int i6, int i7, @Nullable ScanRecord scanRecord, long j) {
        this.device = bluetoothDevice;
        this.eventType = i;
        this.primaryPhy = i2;
        this.secondaryPhy = i3;
        this.advertisingSid = i4;
        this.txPower = i5;
        this.rssi = i6;
        this.periodicAdvertisingInterval = i7;
        this.scanRecord = scanRecord;
        this.timestampNanos = j;
    }

    public ScanResult(Parcel parcel) {
        readFromParcel(parcel);
    }
}
