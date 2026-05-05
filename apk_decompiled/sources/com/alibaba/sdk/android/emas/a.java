package com.alibaba.sdk.android.emas;

import android.util.Base64;
import android.util.Log;
import com.alibaba.ailabs.iot.aisbase.AESUtil;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* JADX INFO: compiled from: AESCrypt.java */
/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private static final byte[] f7a = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static boolean f2876a = false;

    private static SecretKeySpec a(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
        byte[] bytes = str.getBytes("UTF-8");
        messageDigest.update(bytes, 0, bytes.length);
        return new SecretKeySpec(messageDigest.digest(), "AES");
    }

    public static String a(String str, String str2) throws GeneralSecurityException {
        try {
            return Base64.encodeToString(a(a(str), f7a, str2.getBytes("UTF-8")), 2);
        } catch (UnsupportedEncodingException e) {
            if (!f2876a) {
                return null;
            }
            Log.e("AESCrypt", "UnsupportedEncodingException ", e);
            return null;
        }
    }

    public static byte[] a(SecretKeySpec secretKeySpec, byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AESUtil.PKCS7PADDING_CIPHER_ALGORITHM);
        cipher.init(1, secretKeySpec, new IvParameterSpec(bArr));
        return cipher.doFinal(bArr2);
    }

    public static String b(String str, String str2) throws GeneralSecurityException {
        try {
            return new String(b(a(str), f7a, Base64.decode(str2, 2)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            if (!f2876a) {
                return null;
            }
            Log.e("AESCrypt", "UnsupportedEncodingException ", e);
            return null;
        }
    }

    public static byte[] b(SecretKeySpec secretKeySpec, byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AESUtil.PKCS7PADDING_CIPHER_ALGORITHM);
        cipher.init(2, secretKeySpec, new IvParameterSpec(bArr));
        return cipher.doFinal(bArr2);
    }
}
