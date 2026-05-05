package com.alibaba.ailabs.tg.utils;

import java.io.UnsupportedEncodingException;

/* JADX INFO: loaded from: classes.dex */
public final class XxteaUtils {
    private static final int DELTA = -1640531527;

    private static int MX(int i, int i2, int i3, int i4, int i5, int[] iArr) {
        return ((i ^ i2) + (iArr[(i4 & 3) ^ i5] ^ i3)) ^ (((i3 >>> 5) ^ (i2 << 2)) + ((i2 >>> 3) ^ (i3 << 4)));
    }

    private XxteaUtils() {
    }

    public static final byte[] encrypt(byte[] bArr, byte[] bArr2) {
        return bArr.length == 0 ? bArr : toByteArray(encrypt(toIntArray(bArr, true), toIntArray(fixKey(bArr2), false)), false);
    }

    public static final byte[] encrypt(String str, byte[] bArr) {
        try {
            return encrypt(str.getBytes("UTF-8"), bArr);
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static final byte[] encrypt(byte[] bArr, String str) {
        try {
            return encrypt(bArr, str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static final byte[] encrypt(String str, String str2) {
        try {
            return encrypt(str.getBytes("UTF-8"), str2.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static final String encryptToBase64String(byte[] bArr, byte[] bArr2) {
        byte[] bArrEncrypt = encrypt(bArr, bArr2);
        if (bArrEncrypt == null) {
            return null;
        }
        return EncodeUtils.base64Encode2String(bArrEncrypt);
    }

    public static final String encryptToBase64String(String str, byte[] bArr) {
        byte[] bArrEncrypt = encrypt(str, bArr);
        if (bArrEncrypt == null) {
            return null;
        }
        return EncodeUtils.base64Encode2String(bArrEncrypt);
    }

    public static final String encryptToBase64String(byte[] bArr, String str) {
        byte[] bArrEncrypt = encrypt(bArr, str);
        if (bArrEncrypt == null) {
            return null;
        }
        return EncodeUtils.base64Encode2String(bArrEncrypt);
    }

    public static final String encryptToBase64String(String str, String str2) {
        byte[] bArrEncrypt = encrypt(str, str2);
        if (bArrEncrypt == null) {
            return null;
        }
        return EncodeUtils.base64Encode2String(bArrEncrypt);
    }

    public static final byte[] decrypt(byte[] bArr, byte[] bArr2) {
        return bArr.length == 0 ? bArr : toByteArray(decrypt(toIntArray(bArr, false), toIntArray(fixKey(bArr2), false)), true);
    }

    public static final byte[] decrypt(byte[] bArr, String str) {
        try {
            return decrypt(bArr, str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static final byte[] decryptBase64String(String str, byte[] bArr) {
        return decrypt(EncodeUtils.base64Decode(str), bArr);
    }

    public static final byte[] decryptBase64String(String str, String str2) {
        return decrypt(EncodeUtils.base64Decode(str), str2);
    }

    public static final String decryptToString(byte[] bArr, byte[] bArr2) {
        try {
            byte[] bArrDecrypt = decrypt(bArr, bArr2);
            if (bArrDecrypt == null) {
                return null;
            }
            return new String(bArrDecrypt, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static final String decryptToString(byte[] bArr, String str) {
        try {
            byte[] bArrDecrypt = decrypt(bArr, str);
            if (bArrDecrypt == null) {
                return null;
            }
            return new String(bArrDecrypt, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static final String decryptBase64StringToString(String str, byte[] bArr) {
        try {
            byte[] bArrDecrypt = decrypt(EncodeUtils.base64Decode(str), bArr);
            if (bArrDecrypt == null) {
                return null;
            }
            return new String(bArrDecrypt, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static final String decryptBase64StringToString(String str, String str2) {
        try {
            byte[] bArrDecrypt = decrypt(EncodeUtils.base64Decode(str), str2);
            if (bArrDecrypt == null) {
                return null;
            }
            return new String(bArrDecrypt, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    private static int[] encrypt(int[] iArr, int[] iArr2) {
        int length = iArr.length - 1;
        if (length < 1) {
            return iArr;
        }
        int i = (52 / (length + 1)) + 6;
        int iMX = iArr[length];
        int i2 = 0;
        while (true) {
            int i3 = i - 1;
            if (i <= 0) {
                return iArr;
            }
            int i4 = DELTA + i2;
            int i5 = (i4 >>> 2) & 3;
            int iMX2 = iMX;
            int i6 = 0;
            while (i6 < length) {
                int i7 = i6 + 1;
                iMX2 = iArr[i6] + MX(i4, iArr[i7], iMX2, i6, i5, iArr2);
                iArr[i6] = iMX2;
                i6 = i7;
            }
            iMX = iArr[length] + MX(i4, iArr[0], iMX2, i6, i5, iArr2);
            iArr[length] = iMX;
            i2 = i4;
            i = i3;
        }
    }

    private static int[] decrypt(int[] iArr, int[] iArr2) {
        int length = iArr.length - 1;
        if (length < 1) {
            return iArr;
        }
        int iMX = iArr[0];
        for (int i = ((52 / (length + 1)) + 6) * DELTA; i != 0; i -= DELTA) {
            int i2 = (i >>> 2) & 3;
            int iMX2 = iMX;
            int i3 = length;
            while (i3 > 0) {
                iMX2 = iArr[i3] - MX(i, iMX2, iArr[i3 - 1], i3, i2, iArr2);
                iArr[i3] = iMX2;
                i3--;
            }
            iMX = iArr[0] - MX(i, iMX2, iArr[length], i3, i2, iArr2);
            iArr[0] = iMX;
        }
        return iArr;
    }

    private static byte[] fixKey(byte[] bArr) {
        if (bArr.length == 16) {
            return bArr;
        }
        byte[] bArr2 = new byte[16];
        if (bArr.length < 16) {
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        } else {
            System.arraycopy(bArr, 0, bArr2, 0, 16);
        }
        return bArr2;
    }

    private static int[] toIntArray(byte[] bArr, boolean z) {
        int[] iArr;
        int length = (bArr.length & 3) == 0 ? bArr.length >>> 2 : (bArr.length >>> 2) + 1;
        if (z) {
            iArr = new int[length + 1];
            iArr[length] = bArr.length;
        } else {
            iArr = new int[length];
        }
        int length2 = bArr.length;
        for (int i = 0; i < length2; i++) {
            int i2 = i >>> 2;
            iArr[i2] = iArr[i2] | ((bArr[i] & 255) << ((i & 3) << 3));
        }
        return iArr;
    }

    private static byte[] toByteArray(int[] iArr, boolean z) {
        int i;
        int length = iArr.length << 2;
        if (z) {
            i = iArr[iArr.length - 1];
            int i2 = length - 4;
            if (i < i2 - 3 || i > i2) {
                return null;
            }
        } else {
            i = length;
        }
        byte[] bArr = new byte[i];
        for (int i3 = 0; i3 < i; i3++) {
            bArr[i3] = (byte) (iArr[i3 >>> 2] >>> ((i3 & 3) << 3));
        }
        return bArr;
    }
}
