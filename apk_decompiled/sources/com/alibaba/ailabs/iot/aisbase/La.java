package com.alibaba.ailabs.iot.aisbase;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.ailabs.iot.aisbase.spec.DeviceAbility;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: DeviceAbility.java */
/* JADX INFO: loaded from: classes.dex */
public class La implements Parcelable.Creator<DeviceAbility> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public DeviceAbility createFromParcel(Parcel parcel) {
        return new DeviceAbility(parcel, null);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public DeviceAbility[] newArray(int i) {
        return new DeviceAbility[i];
    }
}
