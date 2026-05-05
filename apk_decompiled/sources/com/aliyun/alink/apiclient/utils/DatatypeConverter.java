package com.aliyun.alink.apiclient.utils;

/* JADX INFO: loaded from: classes.dex */
public class DatatypeConverter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final char[] encodeMap = initEncodeMap();

    public static String printBase64Binary(byte[] bArr) {
        return _printBase64Binary(bArr);
    }

    public static byte[] parseBase64Binary(String str) {
        return new byte[0];
    }

    private static char[] initEncodeMap() {
        int i;
        int i2;
        char[] cArr = new char[64];
        int i3 = 0;
        while (true) {
            i = 26;
            if (i3 >= 26) {
                break;
            }
            cArr[i3] = (char) (i3 + 65);
            i3++;
        }
        while (true) {
            if (i >= 52) {
                break;
            }
            cArr[i] = (char) ((i - 26) + 97);
            i++;
        }
        for (i2 = 52; i2 < 62; i2++) {
            cArr[i2] = (char) ((i2 - 52) + 48);
        }
        cArr[62] = '+';
        cArr[63] = '/';
        return cArr;
    }

    public static char encode(int i) {
        return encodeMap[i & 63];
    }

    public static String _printBase64Binary(byte[] bArr) {
        return _printBase64Binary(bArr, 0, bArr.length);
    }

    public static String _printBase64Binary(byte[] bArr, int i, int i2) {
        char[] cArr = new char[((i2 + 2) / 3) * 4];
        _printBase64Binary(bArr, i, i2, cArr, 0);
        return new String(cArr);
    }

    public static int _printBase64Binary(byte[] bArr, int i, int i2, char[] cArr, int i3) {
        while (i2 >= 3) {
            int i4 = i3 + 1;
            cArr[i3] = encode(bArr[i] >> 2);
            int i5 = i4 + 1;
            int i6 = i + 1;
            cArr[i4] = encode(((bArr[i] & 3) << 4) | ((bArr[i6] >> 4) & 15));
            int i7 = i5 + 1;
            int i8 = i + 2;
            cArr[i5] = encode((3 & (bArr[i8] >> 6)) | ((bArr[i6] & 15) << 2));
            i3 = i7 + 1;
            cArr[i7] = encode(bArr[i8] & 63);
            i2 -= 3;
            i += 3;
        }
        if (i2 == 1) {
            int i9 = i3 + 1;
            cArr[i3] = encode(bArr[i] >> 2);
            int i10 = i9 + 1;
            cArr[i9] = encode((bArr[i] & 3) << 4);
            int i11 = i10 + 1;
            cArr[i10] = '=';
            i3 = i11 + 1;
            cArr[i11] = '=';
        }
        if (i2 != 2) {
            return i3;
        }
        int i12 = i3 + 1;
        cArr[i3] = encode(bArr[i] >> 2);
        int i13 = i12 + 1;
        int i14 = (3 & bArr[i]) << 4;
        int i15 = i + 1;
        cArr[i12] = encode(i14 | ((bArr[i15] >> 4) & 15));
        int i16 = i13 + 1;
        cArr[i13] = encode((bArr[i15] & 15) << 2);
        int i17 = i16 + 1;
        cArr[i16] = '=';
        return i17;
    }
}
