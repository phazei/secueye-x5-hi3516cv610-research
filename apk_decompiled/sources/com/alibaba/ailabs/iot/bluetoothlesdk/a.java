package com.alibaba.ailabs.iot.bluetoothlesdk;

import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: compiled from: CompatibleUtils.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f2726a = "a";

    public static byte[] a(byte[] bArr) {
        if (bArr == null || bArr.length < 16) {
            LogUtils.w(f2726a, "Invalid unprovision adv data");
            return null;
        }
        int i = (bArr[0] & 255) + 0 + ((bArr[1] & 255) << 8);
        byte b2 = bArr[2];
        if (424 != i) {
            LogUtils.w(f2726a, "Invalid unprovision node, not ali node");
            return null;
        }
        byte b3 = bArr[3];
        byte b4 = bArr[4];
        byte b5 = bArr[5];
        byte b6 = bArr[6];
        byte[] bArr2 = new byte[14];
        bArr2[0] = -88;
        bArr2[1] = 1;
        bArr2[2] = -123;
        bArr2[3] = 12;
        System.arraycopy(bArr, 3, bArr2, 4, 4);
        System.arraycopy(bArr, 7, bArr2, 8, 6);
        return bArr2;
    }
}
