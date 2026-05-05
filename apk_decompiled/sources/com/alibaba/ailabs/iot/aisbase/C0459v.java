package com.alibaba.ailabs.iot.aisbase;

import aisscanner.ScanFilter;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.v, reason: case insensitive filesystem */
/* JADX INFO: compiled from: ScanFilter.java */
/* JADX INFO: loaded from: classes.dex */
public class C0459v implements Parcelable.Creator<ScanFilter> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ScanFilter createFromParcel(Parcel parcel) {
        ScanFilter.Builder builder = new ScanFilter.Builder();
        if (parcel.readInt() == 1) {
            builder.setDeviceName(parcel.readString());
        }
        if (parcel.readInt() == 1) {
            builder.setDeviceAddress(parcel.readString());
        }
        if (parcel.readInt() == 1) {
            ParcelUuid parcelUuid = (ParcelUuid) parcel.readParcelable(ParcelUuid.class.getClassLoader());
            builder.setServiceUuid(parcelUuid);
            if (parcel.readInt() == 1) {
                builder.setServiceUuid(parcelUuid, (ParcelUuid) parcel.readParcelable(ParcelUuid.class.getClassLoader()));
            }
        }
        if (parcel.readInt() == 1) {
            ParcelUuid parcelUuid2 = (ParcelUuid) parcel.readParcelable(ParcelUuid.class.getClassLoader());
            if (parcel.readInt() == 1) {
                byte[] bArr = new byte[parcel.readInt()];
                parcel.readByteArray(bArr);
                if (parcel.readInt() == 0) {
                    builder.setServiceData(parcelUuid2, bArr);
                } else {
                    byte[] bArr2 = new byte[parcel.readInt()];
                    parcel.readByteArray(bArr2);
                    builder.setServiceData(parcelUuid2, bArr, bArr2);
                }
            }
        }
        int i = parcel.readInt();
        if (parcel.readInt() == 1) {
            byte[] bArr3 = new byte[parcel.readInt()];
            parcel.readByteArray(bArr3);
            if (parcel.readInt() == 0) {
                builder.setManufacturerData(i, bArr3);
            } else {
                byte[] bArr4 = new byte[parcel.readInt()];
                parcel.readByteArray(bArr4);
                builder.setManufacturerData(i, bArr3, bArr4);
            }
        }
        return builder.build();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ScanFilter[] newArray(int i) {
        return new ScanFilter[i];
    }
}
