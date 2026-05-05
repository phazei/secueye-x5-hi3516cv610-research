package com.aliyun.alink.linksdk.securesigner.util;

import android.util.Base64;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes2.dex */
public class Utils {
    private static final String CHARSETNAME = "UTF-8";
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final String HmacSHA1 = "HmacSHA1";
    public static final String HmacSHA256 = "HmacSHA256";
    public static final String MD5 = "MD5";
    private static final String TAG = "DeviceDiagnose";

    public static String getMD5String(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] bArrDigest = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b2 : bArrDigest) {
                String hexString = Integer.toHexString(b2 & 255);
                if (hexString.length() == 1) {
                    sb.append('0');
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported.", e);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("Failed to load MD5 digest.", e2);
        }
    }

    public static String hmacSha1(String str, String str2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            byte[] bArrDoFinal = mac.doFinal(str2.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; bArrDoFinal != null && i < bArrDoFinal.length; i++) {
                String hexString = Integer.toHexString(bArrDoFinal[i] & 255);
                if (hexString.length() == 1) {
                    sb.append('0');
                }
                sb.append(hexString);
            }
            return sb.toString().toUpperCase();
        } catch (Exception unused) {
            Log.e(TAG, "hmacSha1 fail");
            return "";
        }
    }

    public static String hmacSha256(byte[] bArr, byte[] bArr2) {
        try {
            return bytesToHexString(hmacSha1(bArr, bArr2));
        } catch (Throwable unused) {
            return "";
        }
    }

    private static byte[] hmacSha1(byte[] bArr, byte[] bArr2) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "HmacSHA256");
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            return mac.doFinal(bArr2);
        } catch (InvalidKeyException | NoSuchAlgorithmException unused) {
            return null;
        }
    }

    public static String bytesToHexString(byte[] bArr) {
        return bArr == null ? "" : bytesToHexString(bArr, DIGITS_LOWER);
    }

    private static String bytesToHexString(byte[] bArr, char[] cArr) {
        int length = bArr.length;
        char[] cArr2 = new char[length << 1];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr2[i] = cArr[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr2[i3] = cArr[bArr[i2] & 15];
        }
        return new String(cArr2);
    }

    public static String hexStr2Base64Str(String str) {
        int length = str.length() / 2;
        if (1 == length) {
            throw new RuntimeException("invalid hex string : " + str);
        }
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (Integer.valueOf(str.substring(i2, i2 + 2), 16).intValue() & 255);
        }
        return new String(Base64.encode(bArr, 0), StandardCharsets.UTF_8);
    }

    public static boolean hasSecurityGuardDep() {
        try {
            Class.forName("com.alibaba.wireless.security.open.SecurityGuardManager");
            return true;
        } catch (Exception unused) {
            Log.d(TAG, "No SecurityGuardDep");
            return false;
        }
    }
}
