package com.alibaba.ailabs.iot.aisbase;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: BluetoothDeviceWrapper.java */
/* JADX INFO: loaded from: classes.dex */
public class Ia implements Parcelable.Creator<BluetoothDeviceWrapper> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public BluetoothDeviceWrapper createFromParcel(Parcel parcel) {
        return new BluetoothDeviceWrapper(parcel);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public BluetoothDeviceWrapper[] newArray(int i) {
        return new BluetoothDeviceWrapper[i];
    }
}
