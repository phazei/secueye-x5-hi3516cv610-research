package com.alibaba.sdk.android.emas;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import com.google.android.gms.stats.CodePackage;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;

/* JADX INFO: compiled from: AesGcmCipher.java */
/* JADX INFO: loaded from: classes.dex */
class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final b f2877a = new b();

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private KeyStore f8a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f2878b;

    private b() {
        this.f2878b = true;
        try {
            m14a();
            if (Build.VERSION.SDK_INT >= 23) {
                a(m13a());
            }
        } catch (Throwable unused) {
            this.f2878b = false;
        }
    }

    public static b a() {
        return f2877a;
    }

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    private void m14a() {
        try {
            this.f8a = KeyStore.getInstance("AndroidKeyStore");
            this.f8a.load(null);
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            LogUtil.e(Log.getStackTraceString(e));
            this.f2878b = false;
        }
    }

    private void a(Key key) {
        try {
            if (this.f8a.containsAlias("emas_rest_key")) {
                return;
            }
            this.f8a.setKeyEntry("emas_rest_key", key, null, null);
        } catch (KeyStoreException e) {
            LogUtil.e(Log.getStackTraceString(e));
            this.f2878b = false;
        }
    }

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    private Key m13a() {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                return null;
            }
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder("emas_rest_key", 3);
            builder.setKeySize(256);
            builder.setBlockModes(CodePackage.GCM);
            builder.setEncryptionPaddings("NoPadding");
            if (Build.VERSION.SDK_INT >= 28) {
                builder.setUnlockedDeviceRequired(true);
            }
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(builder.build());
            return keyGenerator.generateKey();
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
            LogUtil.e(Log.getStackTraceString(e));
            this.f2878b = false;
            return null;
        }
    }

    byte[] a(byte[] bArr) {
        if (!this.f2878b) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(1, this.f8a.getKey("emas_rest_key", null));
            byte[] iv = cipher.getIV();
            byte[] bArr2 = new byte[cipher.getOutputSize(bArr.length) + 12];
            System.arraycopy(iv, 0, bArr2, 0, 12);
            cipher.doFinal(bArr, 0, bArr.length, bArr2, 12);
            return bArr2;
        } catch (InvalidKeyException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | ShortBufferException e) {
            LogUtil.e(Log.getStackTraceString(e));
            return null;
        }
    }

    byte[] b(byte[] bArr) {
        if (!this.f2878b) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(2, this.f8a.getKey("emas_rest_key", null), new GCMParameterSpec(128, bArr, 0, 12));
            byte[] bArr2 = new byte[cipher.getOutputSize(bArr.length - 12)];
            cipher.doFinal(bArr, 12, bArr.length - 12, bArr2, 0);
            return bArr2;
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | ShortBufferException e) {
            LogUtil.e(Log.getStackTraceString(e));
            return null;
        }
    }
}
