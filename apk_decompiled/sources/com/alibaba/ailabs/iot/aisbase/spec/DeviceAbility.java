package com.alibaba.ailabs.iot.aisbase.spec;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.ailabs.iot.aisbase.La;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class DeviceAbility implements Parcelable {
    public static final Parcelable.Creator<DeviceAbility> CREATOR = new La();
    public byte[] mBTMacAddress;
    public BluetoothDeviceWrapper.EncodeFormat mEncodeFormat;
    public byte[] mGMAVersion;
    public byte mMicRoadCount;
    public byte[] mSlaveAddress;
    public boolean mSupportEchoCancellation;
    public boolean mSupportFM;
    public boolean mSupportHFP;
    public boolean mSupportManualVAD;
    public boolean mSupportNoiseReduction;
    public boolean mSupportPlayer;
    public boolean mSupportVAD;
    public boolean mSupportWakeupWords;

    public /* synthetic */ DeviceAbility(Parcel parcel, La la) {
        this(parcel);
    }

    public static DeviceAbility parseFromBytes(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        DeviceAbility deviceAbility = new DeviceAbility();
        if (bArr.length >= 1) {
            deviceAbility.mSupportWakeupWords = (bArr[0] & 1) == 1;
            deviceAbility.mSupportVAD = ((bArr[0] >> 1) & 1) == 1;
            deviceAbility.mSupportEchoCancellation = ((bArr[0] >> 2) & 1) == 1;
            deviceAbility.mSupportNoiseReduction = ((bArr[0] >> 3) & 1) == 1;
            deviceAbility.mSupportPlayer = ((bArr[0] >> 4) & 1) == 1;
            deviceAbility.mSupportFM = ((bArr[0] >> 5) & 1) == 1;
            deviceAbility.mSupportHFP = ((bArr[0] >> 6) & 1) == 1;
            deviceAbility.mSupportManualVAD = ((bArr[0] >> 7) & 1) == 1;
            if (bArr.length >= 2) {
                deviceAbility.mMicRoadCount = (byte) (bArr[1] & 7);
            }
            if (bArr.length >= 3) {
                deviceAbility.mEncodeFormat = BluetoothDeviceWrapper.EncodeFormat.parseFromInt(bArr[2]);
            }
            if (bArr.length >= 11) {
                deviceAbility.mGMAVersion = Arrays.copyOfRange(bArr, 3, 5);
                deviceAbility.mBTMacAddress = new byte[6];
                for (int i = 0; i < 6; i++) {
                    deviceAbility.mBTMacAddress[i] = bArr[10 - i];
                }
                if (bArr.length >= 17) {
                    deviceAbility.mSlaveAddress = new byte[6];
                    for (int i2 = 0; i2 < 6; i2++) {
                        deviceAbility.mSlaveAddress[i2] = bArr[16 - i2];
                    }
                }
            }
        }
        return deviceAbility;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "DeviceAbility{mSupportWakeupWords=" + this.mSupportWakeupWords + ", mSupportVAD=" + this.mSupportVAD + ", mSupportEchoCancellation=" + this.mSupportEchoCancellation + ", mSupportNoiseReduction=" + this.mSupportNoiseReduction + ", mSupportPlayer=" + this.mSupportPlayer + ", mSupportFM=" + this.mSupportFM + ", mSupportHFP=" + this.mSupportHFP + ", mGMAVersion=" + Arrays.toString(this.mGMAVersion) + ", mBTMacAddress=" + Arrays.toString(this.mBTMacAddress) + ", mSlaveAddress=" + Arrays.toString(this.mSlaveAddress) + ", mEncodeFormat=" + this.mEncodeFormat + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.mSupportWakeupWords ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mSupportVAD ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mSupportEchoCancellation ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mSupportNoiseReduction ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mSupportPlayer ? (byte) 1 : (byte) 0);
        parcel.writeSerializable(this.mEncodeFormat);
        parcel.writeByte(this.mSupportFM ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mSupportHFP ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mSupportManualVAD ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.mGMAVersion.length);
        parcel.writeByteArray(this.mGMAVersion);
        parcel.writeInt(this.mBTMacAddress.length);
        parcel.writeByteArray(this.mBTMacAddress);
    }

    public DeviceAbility() {
        this.mSupportWakeupWords = false;
        this.mSupportVAD = false;
        this.mSupportEchoCancellation = false;
        this.mSupportNoiseReduction = false;
        this.mSupportPlayer = false;
        this.mSupportFM = false;
        this.mSupportHFP = false;
        this.mSupportManualVAD = false;
        this.mMicRoadCount = (byte) 1;
        this.mGMAVersion = new byte[0];
        this.mBTMacAddress = new byte[0];
        this.mSlaveAddress = new byte[0];
    }

    public DeviceAbility(Parcel parcel) {
        this.mSupportWakeupWords = false;
        this.mSupportVAD = false;
        this.mSupportEchoCancellation = false;
        this.mSupportNoiseReduction = false;
        this.mSupportPlayer = false;
        this.mSupportFM = false;
        this.mSupportHFP = false;
        this.mSupportManualVAD = false;
        this.mMicRoadCount = (byte) 1;
        this.mGMAVersion = new byte[0];
        this.mBTMacAddress = new byte[0];
        this.mSlaveAddress = new byte[0];
        this.mSupportWakeupWords = parcel.readByte() != 0;
        this.mSupportVAD = parcel.readByte() != 0;
        this.mSupportEchoCancellation = parcel.readByte() != 0;
        this.mSupportNoiseReduction = parcel.readByte() != 0;
        this.mSupportPlayer = parcel.readByte() != 0;
        this.mEncodeFormat = (BluetoothDeviceWrapper.EncodeFormat) parcel.readSerializable();
        this.mSupportFM = parcel.readByte() != 0;
        this.mSupportHFP = parcel.readByte() != 0;
        this.mSupportManualVAD = parcel.readByte() != 0;
        this.mGMAVersion = new byte[parcel.readInt()];
        parcel.readByteArray(this.mGMAVersion);
        this.mBTMacAddress = new byte[parcel.readInt()];
        parcel.readByteArray(this.mBTMacAddress);
    }
}
