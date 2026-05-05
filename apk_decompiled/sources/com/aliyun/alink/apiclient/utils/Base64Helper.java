package com.aliyun.alink.apiclient.utils;

import com.huawei.hms.framework.common.ContainerUtils;
import java.io.UnsupportedEncodingException;

/* JADX INFO: loaded from: classes.dex */
public class Base64Helper {
    private static final String BASE64_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static final int[] BASE64_DECODE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    private static byte[] zeroPad(int i, byte[] bArr) {
        byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    public static synchronized String encode(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        int length = (3 - (bArr.length % 3)) % 3;
        byte[] bArrZeroPad = zeroPad(bArr.length + length, bArr);
        for (int i = 0; i < bArrZeroPad.length; i += 3) {
            int i2 = ((bArrZeroPad[i] & 255) << 16) + ((bArrZeroPad[i + 1] & 255) << 8) + (bArrZeroPad[i + 2] & 255);
            sb.append(BASE64_CODE.charAt((i2 >> 18) & 63));
            sb.append(BASE64_CODE.charAt((i2 >> 12) & 63));
            sb.append(BASE64_CODE.charAt((i2 >> 6) & 63));
            sb.append(BASE64_CODE.charAt(i2 & 63));
        }
        int length2 = sb.length();
        while (length > 0) {
            sb.setCharAt(length2 - length, '=');
            length--;
        }
        return sb.toString();
    }

    public static synchronized String encode(String str, String str2) throws UnsupportedEncodingException {
        if (str == null || str2 == null) {
            return null;
        }
        return encode(str.getBytes(str2));
    }

    public static synchronized String decode(String str, String str2) throws UnsupportedEncodingException {
        if (str == null || str2 == null) {
            return null;
        }
        int length = str.endsWith("==") ? str.length() - 2 : str.endsWith(ContainerUtils.KEY_VALUE_DELIMITER) ? str.length() - 1 : str.length();
        byte[] bArr = new byte[(length * 3) / 4];
        int i = length - (length % 4);
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            int i4 = BASE64_DECODE[str.charAt(i2)];
            int i5 = BASE64_DECODE[str.charAt(i2 + 1)];
            int i6 = BASE64_DECODE[str.charAt(i2 + 2)];
            int i7 = BASE64_DECODE[str.charAt(i2 + 3)];
            int i8 = i3 + 1;
            bArr[i3] = (byte) (((i4 << 2) | (i5 >> 4)) & 255);
            int i9 = i8 + 1;
            bArr[i8] = (byte) ((((i5 & 15) << 4) | (i6 >> 2)) & 255);
            bArr[i9] = (byte) ((((i6 & 3) << 6) | i7) & 255);
            i2 += 4;
            i3 = i9 + 1;
        }
        if (2 <= length % 4) {
            int i10 = BASE64_DECODE[str.charAt(i)];
            int i11 = BASE64_DECODE[str.charAt(i + 1)];
            int i12 = i3 + 1;
            bArr[i3] = (byte) (((i10 << 2) | (i11 >> 4)) & 255);
            if (3 == length % 4) {
                bArr[i12] = (byte) (((BASE64_DECODE[str.charAt(i + 2)] >> 2) | ((i11 & 15) << 4)) & 255);
            }
        }
        return new String(bArr, str2);
    }
}
