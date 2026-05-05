package com.aliyun.alink.business.devicecenter.utils;

import java.io.ByteArrayOutputStream;

/* JADX INFO: loaded from: classes2.dex */
public class HexUtils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f3764a = "0123456789ABCDEF";

    public static byte[] HexString2Bytes(String str) {
        byte[] bArr = new byte[6];
        byte[] bytes = str.getBytes();
        for (int i = 0; i < 6; i++) {
            int i2 = i * 2;
            bArr[i] = a(bytes[i2], bytes[i2 + 1]);
        }
        return bArr;
    }

    public static byte a(byte b2, byte b3) {
        return (byte) (((byte) (Byte.decode("0x" + new String(new byte[]{b2})).byteValue() << 4)) | Byte.decode("0x" + new String(new byte[]{b3})).byteValue());
    }

    public static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(128);
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static String decode(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(str.length() / 2);
        for (int i = 0; i < str.length(); i += 2) {
            byteArrayOutputStream.write((f3764a.indexOf(str.charAt(i)) << 4) | f3764a.indexOf(str.charAt(i + 1)));
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    public static String encode(String str) {
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b2 : bytes) {
            sb.append(f3764a.charAt((b2 & 240) >> 4));
            sb.append(f3764a.charAt((b2 & 15) >> 0));
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String upperCase = str.toUpperCase();
        int length = upperCase.length() / 2;
        char[] charArray = upperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (a(charArray[i2 + 1]) | (a(charArray[i2]) << 4));
        }
        return bArr;
    }

    public static String stringToHexString(String str) {
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            str2 = str2 + Integer.toHexString(str.charAt(i));
        }
        return str2;
    }

    public static byte a(char c2) {
        return (byte) "0123456789ABCDEF".indexOf(c2);
    }
}
