package com.alibaba.sdk.android.tbrest.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes.dex */
public class MD5Utils {
    public static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b2 : bArr) {
            sb.append(hexChar[(b2 & 240) >>> 4]);
            sb.append(hexChar[b2 & 15]);
        }
        return sb.toString();
    }

    public static String getMd5Hex(byte[] bArr) {
        byte[] md5 = getMd5(bArr);
        return md5 != null ? toHexString(md5) : "0000000000000000";
    }

    public static byte[] getMd5(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bArr);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
