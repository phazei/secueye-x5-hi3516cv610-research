package com.alibaba.ailabs.iot.aisbase.spec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TLV {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public byte f2675a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public byte f2676b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public byte[] f2677c;

    public TLV() {
    }

    public static List<TLV> parseMultiFromBytes(byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        int length = 0;
        int length2 = bArr == null ? 0 : bArr.length;
        while (true) {
            TLV singleFromBytes = parseSingleFromBytes(bArr, length, length2);
            if (singleFromBytes == null) {
                return arrayList;
            }
            arrayList.add(singleFromBytes);
            length += singleFromBytes.getLength() + 2;
        }
    }

    public static TLV parseSingleFromBytes(byte[] bArr, int i, int i2) {
        int i3;
        if (bArr != null && bArr.length != 0 && i < bArr.length) {
            TLV tlv = new TLV();
            if (i2 > bArr.length) {
                i2 = bArr.length;
            }
            byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, i, i2);
            tlv.setType(bArrCopyOfRange[0]);
            byte b2 = bArrCopyOfRange.length > 1 ? bArrCopyOfRange[1] : (byte) 0;
            if (b2 >= 0 && bArrCopyOfRange.length >= (i3 = b2 + 2)) {
                tlv.setLength(b2);
                tlv.setValue(Arrays.copyOfRange(bArrCopyOfRange, 2, i3));
                return tlv;
            }
        }
        return null;
    }

    public byte getLength() {
        return this.f2676b;
    }

    public byte getType() {
        return this.f2675a;
    }

    public byte[] getValue() {
        return this.f2677c;
    }

    public void setLength(byte b2) {
        this.f2676b = b2;
    }

    public void setType(byte b2) {
        this.f2675a = b2;
    }

    public void setValue(byte[] bArr) {
        this.f2677c = bArr;
    }

    public byte[] toBytes() {
        byte[] bArr = this.f2677c;
        byte[] bArr2 = new byte[(bArr == null ? 0 : bArr.length) + 2];
        bArr2[0] = this.f2675a;
        bArr2[1] = this.f2676b;
        byte[] bArr3 = this.f2677c;
        if (bArr3 != null && bArr3.length > 0) {
            System.arraycopy(bArr3, 0, bArr2, 2, bArr3.length);
        }
        return bArr2;
    }

    public TLV(byte b2, byte b3, byte[] bArr) {
        this.f2675a = b2;
        this.f2676b = b3;
        this.f2677c = bArr;
    }
}
