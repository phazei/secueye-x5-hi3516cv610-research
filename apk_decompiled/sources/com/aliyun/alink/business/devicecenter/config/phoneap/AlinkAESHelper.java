package com.aliyun.alink.business.devicecenter.config.phoneap;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.AlinkWifiSolutionUtils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
public class AlinkAESHelper {
    public static String a(String str, byte[] bArr, byte[] bArr2) {
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
            return AlinkWifiSolutionUtils.bytesToHexString(bArrEncryptCBCMode);
        } catch (Exception e) {
            ALog.d("AlinkAESHelper", "encryptCBC(),error, e=" + e.toString());
            return null;
        }
    }

    public static byte[] decryptCBCMode(byte[] bArr, byte[] bArr2, byte[] bArr3) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(2, secretKeySpec, new IvParameterSpec(bArr2));
        return cipher.doFinal(bArr3);
    }

    public static String encrypt128CBC(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return null;
        }
        return a(str, AlinkWifiSolutionUtils.hexStringTobytes(str2), AlinkWifiSolutionUtils.hexStringTobytes(str3));
    }

    public static byte[] encrypt128CFB(String str, String str2) {
        ALog.d("AlinkAESHelper", "encrypt128CFB(), AK = " + str2);
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            byte[] bArrEncryptCFBMode = encryptCFBMode(AlinkWifiSolutionUtils.hexStringTobytes(str2), new byte[16], str.getBytes());
            if (bArrEncryptCFBMode.length == 0) {
                return null;
            }
            return bArrEncryptCFBMode;
        } catch (Exception e) {
            ALog.d("AlinkAESHelper", "encryptCBC(),error, e=" + e.toString());
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

    public static byte[] encryptCFBMode(byte[] bArr, byte[] bArr2, byte[] bArr3) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        cipher.getBlockSize();
        cipher.init(1, secretKeySpec, new IvParameterSpec(bArr2));
        return cipher.doFinal(bArr3);
    }
}
