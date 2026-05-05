package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
public class AESUtil {
    public static final String PKCS7PADDING_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2458a = "AESUtil";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static AESUtil f2459b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f2460c = "AES/CBC/NoPadding";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public SecretKeySpec f2461d = null;
    public byte[] e = "123aqwed#*$!(4ju".getBytes();
    public String g = "";
    public IvParameterSpec f = new IvParameterSpec(this.e);

    public static AESUtil getInstance() {
        if (f2459b == null) {
            synchronized (AESUtil.class) {
                if (f2459b == null) {
                    f2459b = new AESUtil();
                }
            }
        }
        return f2459b;
    }

    public final byte[] a(String str, SecretKey secretKey, IvParameterSpec ivParameterSpec, byte[] bArr) {
        LogUtils.d(f2458a, "Decrypt  cmp: " + str + ", secret key: " + ConvertUtils.bytes2HexString(secretKey.getEncoded()) + ", cipher text: " + ConvertUtils.bytes2HexString(bArr));
        try {
            Cipher cipher = Cipher.getInstance(str);
            cipher.init(2, secretKey, ivParameterSpec);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String str) {
        return ConvertUtils.bytes2HexString(a("AES/CBC/NoPadding", this.f2461d, this.f, ConvertUtils.hexString2Bytes(str)));
    }

    public byte[] encrypt(byte[] bArr) {
        return encrypt("AES/CBC/NoPadding", this.f2461d, this.f, bArr);
    }

    public void setKey(String str) {
        this.g = str;
        this.f2461d = new SecretKeySpec(ConvertUtils.hexString2Bytes(this.g), "AES");
    }

    public byte[] encrypt(String str, byte[] bArr) {
        return encrypt(str, this.f2461d, this.f, bArr);
    }

    public byte[] decrypt(byte[] bArr) {
        return a(PKCS7PADDING_CIPHER_ALGORITHM, this.f2461d, this.f, bArr);
    }

    public byte[] encrypt(String str, SecretKey secretKey, IvParameterSpec ivParameterSpec, byte[] bArr) {
        try {
            Cipher cipher = Cipher.getInstance(str);
            cipher.init(1, secretKey, ivParameterSpec);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setKey(byte[] bArr) {
        this.g = ConvertUtils.bytes2HexString(bArr);
        this.f2461d = new SecretKeySpec(bArr, "AES");
    }
}
