package com.alibaba.fastjson.util;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class Base64 {
    public static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    public static final int[] IA = new int[256];

    static {
        Arrays.fill(IA, -1);
        int length = CA.length;
        for (int i = 0; i < length; i++) {
            IA[CA[i]] = i;
        }
        IA[61] = 0;
    }

    public static byte[] decodeFast(char[] cArr, int i, int i2) {
        int i3;
        int i4 = 0;
        if (i2 == 0) {
            return new byte[0];
        }
        int i5 = (i + i2) - 1;
        while (i < i5 && IA[cArr[i]] < 0) {
            i++;
        }
        while (i5 > 0 && IA[cArr[i5]] < 0) {
            i5--;
        }
        int i6 = cArr[i5] == '=' ? cArr[i5 + (-1)] == '=' ? 2 : 1 : 0;
        int i7 = (i5 - i) + 1;
        if (i2 > 76) {
            i3 = (cArr[76] == '\r' ? i7 / 78 : 0) << 1;
        } else {
            i3 = 0;
        }
        int i8 = (((i7 - i3) * 6) >> 3) - i6;
        byte[] bArr = new byte[i8];
        int i9 = (i8 / 3) * 3;
        int i10 = i;
        int i11 = 0;
        int i12 = 0;
        while (i11 < i9) {
            int[] iArr = IA;
            int i13 = i10 + 1;
            int i14 = i13 + 1;
            int i15 = (iArr[cArr[i10]] << 18) | (iArr[cArr[i13]] << 12);
            int i16 = i14 + 1;
            int i17 = i15 | (iArr[cArr[i14]] << 6);
            int i18 = i16 + 1;
            int i19 = i17 | iArr[cArr[i16]];
            int i20 = i11 + 1;
            bArr[i11] = (byte) (i19 >> 16);
            int i21 = i20 + 1;
            bArr[i20] = (byte) (i19 >> 8);
            int i22 = i21 + 1;
            bArr[i21] = (byte) i19;
            if (i3 > 0 && (i12 = i12 + 1) == 19) {
                i18 += 2;
                i12 = 0;
            }
            i10 = i18;
            i11 = i22;
        }
        if (i11 < i8) {
            int i23 = 0;
            while (i10 <= i5 - i6) {
                i4 |= IA[cArr[i10]] << (18 - (i23 * 6));
                i23++;
                i10++;
            }
            int i24 = 16;
            while (i11 < i8) {
                bArr[i11] = (byte) (i4 >> i24);
                i24 -= 8;
                i11++;
            }
        }
        return bArr;
    }

    public static byte[] decodeFast(String str, int i, int i2) {
        int i3;
        int i4 = 0;
        if (i2 == 0) {
            return new byte[0];
        }
        int i5 = (i + i2) - 1;
        while (i < i5 && IA[str.charAt(i)] < 0) {
            i++;
        }
        while (i5 > 0 && IA[str.charAt(i5)] < 0) {
            i5--;
        }
        int i6 = str.charAt(i5) == '=' ? str.charAt(i5 + (-1)) == '=' ? 2 : 1 : 0;
        int i7 = (i5 - i) + 1;
        if (i2 > 76) {
            i3 = (str.charAt(76) == '\r' ? i7 / 78 : 0) << 1;
        } else {
            i3 = 0;
        }
        int i8 = (((i7 - i3) * 6) >> 3) - i6;
        byte[] bArr = new byte[i8];
        int i9 = (i8 / 3) * 3;
        int i10 = i;
        int i11 = 0;
        int i12 = 0;
        while (i11 < i9) {
            int i13 = i10 + 1;
            int i14 = i13 + 1;
            int i15 = (IA[str.charAt(i10)] << 18) | (IA[str.charAt(i13)] << 12);
            int i16 = i14 + 1;
            int i17 = i15 | (IA[str.charAt(i14)] << 6);
            int i18 = i16 + 1;
            int i19 = i17 | IA[str.charAt(i16)];
            int i20 = i11 + 1;
            bArr[i11] = (byte) (i19 >> 16);
            int i21 = i20 + 1;
            bArr[i20] = (byte) (i19 >> 8);
            int i22 = i21 + 1;
            bArr[i21] = (byte) i19;
            if (i3 > 0 && (i12 = i12 + 1) == 19) {
                i18 += 2;
                i12 = 0;
            }
            i10 = i18;
            i11 = i22;
        }
        if (i11 < i8) {
            int i23 = 0;
            while (i10 <= i5 - i6) {
                i4 |= IA[str.charAt(i10)] << (18 - (i23 * 6));
                i23++;
                i10++;
            }
            int i24 = 16;
            while (i11 < i8) {
                bArr[i11] = (byte) (i4 >> i24);
                i24 -= 8;
                i11++;
            }
        }
        return bArr;
    }

    public static byte[] decodeFast(String str) {
        int i;
        int length = str.length();
        int i2 = 0;
        if (length == 0) {
            return new byte[0];
        }
        int i3 = length - 1;
        int i4 = 0;
        while (i4 < i3 && IA[str.charAt(i4) & 255] < 0) {
            i4++;
        }
        while (i3 > 0 && IA[str.charAt(i3) & 255] < 0) {
            i3--;
        }
        int i5 = str.charAt(i3) == '=' ? str.charAt(i3 + (-1)) == '=' ? 2 : 1 : 0;
        int i6 = (i3 - i4) + 1;
        if (length > 76) {
            i = (str.charAt(76) == '\r' ? i6 / 78 : 0) << 1;
        } else {
            i = 0;
        }
        int i7 = (((i6 - i) * 6) >> 3) - i5;
        byte[] bArr = new byte[i7];
        int i8 = (i7 / 3) * 3;
        int i9 = 0;
        int i10 = i4;
        int i11 = 0;
        while (i11 < i8) {
            int i12 = i10 + 1;
            int i13 = i12 + 1;
            int i14 = (IA[str.charAt(i10)] << 18) | (IA[str.charAt(i12)] << 12);
            int i15 = i13 + 1;
            int i16 = i14 | (IA[str.charAt(i13)] << 6);
            int i17 = i15 + 1;
            int i18 = i16 | IA[str.charAt(i15)];
            int i19 = i11 + 1;
            bArr[i11] = (byte) (i18 >> 16);
            int i20 = i19 + 1;
            bArr[i19] = (byte) (i18 >> 8);
            int i21 = i20 + 1;
            bArr[i20] = (byte) i18;
            if (i > 0 && (i9 = i9 + 1) == 19) {
                i17 += 2;
                i9 = 0;
            }
            i10 = i17;
            i11 = i21;
        }
        if (i11 < i7) {
            int i22 = 0;
            while (i10 <= i3 - i5) {
                i2 |= IA[str.charAt(i10)] << (18 - (i22 * 6));
                i22++;
                i10++;
            }
            int i23 = 16;
            while (i11 < i7) {
                bArr[i11] = (byte) (i2 >> i23);
                i23 -= 8;
                i11++;
            }
        }
        return bArr;
    }
}
