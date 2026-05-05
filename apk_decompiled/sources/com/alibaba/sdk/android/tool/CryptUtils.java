package com.alibaba.sdk.android.tool;

import android.text.TextUtils;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* JADX INFO: loaded from: classes.dex */
public class CryptUtils {
    private static final char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String aesDecrypt(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                return aesDecryptByBytes(a.a(str2), str);
            } catch (Exception unused) {
            }
        }
        return null;
    }

    private static String aesDecryptByBytes(byte[] bArr, String str) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(hash(str, "MD5").substring(0, 16).getBytes("utf-8"), "AES/ECB/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, secretKeySpec);
        return new String(cipher.doFinal(bArr));
    }

    public static String aesEncrypt(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                return a.b(aesEncryptToBytes(str, str2));
            } catch (Exception unused) {
            }
        }
        return null;
    }

    private static byte[] aesEncryptToBytes(String str, String str2) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(hash(str, "MD5").substring(0, 16).getBytes("utf-8"), "AES/ECB/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, secretKeySpec);
        return cipher.doFinal(str2.getBytes("utf-8"));
    }

    public static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b2 : bArr) {
            sb.append(hexChar[(b2 & 240) >>> 4]);
            sb.append(hexChar[b2 & 15]);
        }
        return sb.toString();
    }

    public static String getHmacSha1(String str, String str2) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKeySpec);
        return bytesToHexString(mac.doFinal(str2.getBytes()));
    }

    public static String getMD5String(String str) throws NoSuchAlgorithmException {
        return bytesToHexString(MessageDigest.getInstance("MD5").digest(str.getBytes()));
    }

    public static String getSHA1String(String str) throws NoSuchAlgorithmException {
        return bytesToHexString(MessageDigest.getInstance(MessageDigestAlgorithms.SHA_1).digest(str.getBytes()));
    }

    private static String hash(String str, String str2) {
        if (str == null) {
            return "";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(str2);
            messageDigest.update(str.getBytes());
            return bytesToHexString(messageDigest.digest());
        } catch (Exception unused) {
            return "";
        }
    }
}
