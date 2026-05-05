package com.linkkit.tools.utils;

import android.text.TextUtils;
import com.linkkit.tools.a;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes3.dex */
public class AESHelper {
    private static final int BLOCK_BYTE_SIZE = 16;
    public static final String RANDOM_DEFAULT_IV = "00000000000000000000000000000000";
    private static final String TAG = "AESHelper";

    public static String encrypt128CBC(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return null;
        }
        return encryptCBC(str, StringUtils.hexStringTobytes(str2), StringUtils.hexStringTobytes(str3));
    }

    private static String encryptCBC(String str, byte[] bArr, byte[] bArr2) {
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        int i = length % 16;
        if (i != 0) {
            length += 16 - i;
        }
        byte[] bArr3 = new byte[length];
        System.arraycopy(bytes, 0, bArr3, 0, bytes.length);
        try {
            byte[] bArrEncryptCBCMode = encryptCBCMode(bArr2, bArr, bArr3);
            if (bArrEncryptCBCMode.length == 0) {
                return null;
            }
            return StringUtils.bytesToHexString(bArrEncryptCBCMode);
        } catch (Exception e) {
            a.a(TAG, "encryptCBC(),error, e=" + e.toString());
            return null;
        }
    }

    public static byte[] encryptCBCMode(byte[] bArr, byte[] bArr2, byte[] bArr3) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.getBlockSize();
        cipher.init(1, secretKeySpec, new IvParameterSpec(bArr2));
        return cipher.doFinal(bArr3);
    }

    public static String decrypt128CBC(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return new String(decryptCBCMode(StringUtils.hexStringTobytes(str3), StringUtils.hexStringTobytes(str2), StringUtils.hexStringTobytes(str)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decryptCBCMode(byte[] bArr, byte[] bArr2, byte[] bArr3) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(2, secretKeySpec, new IvParameterSpec(bArr2));
        return cipher.doFinal(bArr3);
    }
}
