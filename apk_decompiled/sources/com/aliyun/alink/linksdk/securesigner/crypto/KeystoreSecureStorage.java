package com.aliyun.alink.linksdk.securesigner.crypto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Base64;
import android.util.Log;
import com.aliyun.alink.linksdk.securesigner.SecurityImpl;
import com.aliyun.alink.linksdk.securesigner.util.AesEncryptor;
import com.aliyun.alink.linksdk.securesigner.util.SafeStorageUtil;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/* JADX INFO: loaded from: classes2.dex */
public class KeystoreSecureStorage implements SecureStorage {
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS7Padding";
    private static final String IV = "_iv";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String KEY_ALIAS = "IOT-KeyAlias";
    private static final String SAFE_KEY_LEVEL21 = "safe-key-level-21";
    private static final String SHARED_PREFS_NAME = "SecureDataStore";
    private static Context context = null;
    private static boolean enableKeyStore = false;
    private static volatile KeystoreSecureStorage instance;
    private String afterEncryption;
    private KeyStore keyStore;
    SecurityImpl securityIml;
    private SharedPreferences sharedPreferences;

    public static KeystoreSecureStorage getInstance(Context context2) {
        if (instance == null) {
            synchronized (KeystoreSecureStorage.class) {
                if (instance == null) {
                    try {
                        context = context2.getApplicationContext();
                        instance = new KeystoreSecureStorage(context);
                    } catch (Exception e) {
                        Log.e("KeystoreSecureStorage", "init failed. e=" + e);
                    }
                }
            }
        }
        return instance;
    }

    private void createKeyIfNotExists() throws Exception {
        if (this.keyStore.containsAlias(KEY_ALIAS)) {
            return;
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", KEYSTORE_PROVIDER);
        if (Build.VERSION.SDK_INT >= 23) {
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS, 3).setBlockModes("CBC").setEncryptionPaddings("PKCS7Padding").build());
        }
        keyGenerator.generateKey();
    }

    private KeystoreSecureStorage(Context context2) {
        this.sharedPreferences = context2.getSharedPreferences(SHARED_PREFS_NAME, 0);
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                this.keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
                this.keyStore.load(null);
                createKeyIfNotExists();
                enableKeyStore = true;
                put("key_store_key", "key_store_value");
                get("key_store_key");
            } catch (Exception e) {
                Log.e("KeystoreSecureStorage", "init failed. e=" + e);
                enableKeyStore = false;
            }
        }
        this.securityIml = new SecurityImpl();
        if (this.securityIml.getAppKey() != null) {
            this.afterEncryption = this.securityIml.sign(SAFE_KEY_LEVEL21 + SafeStorageUtil.getDeviceId(context2), "HmacSHA1").substring(0, 16);
        }
    }

    public String safeEncrypt(String str) {
        try {
            return AesEncryptor.encrypt(this.afterEncryption, str);
        } catch (Exception e) {
            Log.e("KeystoreSecureStorage", "safeEncrypt failed. e=" + e);
            return null;
        }
    }

    public String safeDecrypt(String str) {
        try {
            return AesEncryptor.decrypt(this.afterEncryption, str);
        } catch (Exception e) {
            Log.e("KeystoreSecureStorage", "safeDecrypt failed. e=" + e);
            return null;
        }
    }

    @Override // com.aliyun.alink.linksdk.securesigner.crypto.SecureStorage
    public void put(String str, String str2) throws SecureStorageException {
        SharedPreferences.Editor editorEdit = this.sharedPreferences.edit();
        if (Build.VERSION.SDK_INT >= 23 && enableKeyStore) {
            try {
                SecretKey secretKey = (SecretKey) this.keyStore.getKey(KEY_ALIAS, null);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                cipher.init(1, secretKey);
                editorEdit.putString(str, Base64.encodeToString(cipher.doFinal(str2.getBytes(StandardCharsets.UTF_8)), 0));
                editorEdit.putString(str + IV, Base64.encodeToString(cipher.getIV(), 0)).apply();
            } catch (GeneralSecurityException e) {
                throw new SecureStorageException("Failed to encrypt and save data", e);
            }
        } else {
            try {
                editorEdit.putString(str, AesEncryptor.encrypt(this.afterEncryption, str2));
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
        editorEdit.apply();
    }

    @Override // com.aliyun.alink.linksdk.securesigner.crypto.SecureStorage
    public String get(String str) throws SecureStorageException {
        String string = this.sharedPreferences.getString(str, null);
        String string2 = this.sharedPreferences.getString(str + IV, null);
        if (string == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 23 && enableKeyStore) {
            try {
                SecretKey secretKey = (SecretKey) this.keyStore.getKey(KEY_ALIAS, null);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                cipher.init(2, secretKey, new IvParameterSpec(Base64.decode(string2, 0)));
                return new String(cipher.doFinal(Base64.decode(string, 0)), StandardCharsets.UTF_8);
            } catch (GeneralSecurityException e) {
                throw new SecureStorageException("Failed to decrypt and retrieve data", e);
            }
        }
        try {
            return AesEncryptor.decrypt(this.afterEncryption, string);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // com.aliyun.alink.linksdk.securesigner.crypto.SecureStorage
    public void remove(String str) throws SecureStorageException {
        try {
            this.sharedPreferences.edit().remove(str).apply();
        } catch (Exception e) {
            throw new SecureStorageException("Failed to remove data from secure storage", e);
        }
    }
}
