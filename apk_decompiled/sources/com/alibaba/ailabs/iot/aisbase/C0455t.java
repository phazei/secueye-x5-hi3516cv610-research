package com.alibaba.ailabs.iot.aisbase;

import android.os.ParcelUuid;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.t, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothUuid.java */
/* JADX INFO: loaded from: classes.dex */
public final class C0455t {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final ParcelUuid f2678a = ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB");

    public static ParcelUuid a(byte[] bArr) {
        long j;
        if (bArr == null) {
            throw new IllegalArgumentException("uuidBytes cannot be null");
        }
        int length = bArr.length;
        if (length != 2 && length != 4 && length != 16) {
            throw new IllegalArgumentException("uuidBytes length invalid - " + length);
        }
        if (length == 16) {
            ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
            return new ParcelUuid(new UUID(byteBufferOrder.getLong(8), byteBufferOrder.getLong(0)));
        }
        if (length == 2) {
            j = ((long) (bArr[0] & 255)) + ((long) ((bArr[1] & 255) << 8));
        } else {
            j = ((long) ((bArr[3] & 255) << 24)) + ((long) (bArr[0] & 255)) + ((long) ((bArr[1] & 255) << 8)) + ((long) ((bArr[2] & 255) << 16));
        }
        return new ParcelUuid(new UUID(f2678a.getUuid().getMostSignificantBits() + (j << 32), f2678a.getUuid().getLeastSignificantBits()));
    }
}
