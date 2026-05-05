package com.alibaba.ailabs.iot.aisbase;

import aisble.data.Data;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.a, reason: case insensitive filesystem */
/* JADX INFO: compiled from: Data.java */
/* JADX INFO: loaded from: classes.dex */
public class C0417a implements Parcelable.Creator<Data> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public Data createFromParcel(Parcel parcel) {
        return new Data(parcel);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public Data[] newArray(int i) {
        return new Data[i];
    }
}
