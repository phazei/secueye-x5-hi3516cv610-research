package tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* JADX INFO: compiled from: SecuritySharedPreference.java */
/* JADX INFO: loaded from: classes4.dex */
class EncryptUtil {
    private static final String TAG = "EncryptUtil";
    private static EncryptUtil instance;
    private String key;

    private EncryptUtil(Context context) {
        this.key = SHA(getDeviceSerialNumber(context) + "#$ERDTS$D%F^Gojikbh").substring(0, 16);
    }

    public static EncryptUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (EncryptUtil.class) {
                if (instance == null) {
                    instance = new EncryptUtil(context);
                }
            }
        }
        return instance;
    }

    @SuppressLint({"HardwareIds"})
    private String getDeviceSerialNumber(Context context) {
        try {
            String str = (String) Build.class.getField("SERIAL").get(null);
            return TextUtils.isEmpty(str) ? Settings.Secure.getString(context.getContentResolver(), "android_id") : str;
        } catch (Exception unused) {
            return Settings.Secure.getString(context.getContentResolver(), "android_id");
        }
    }

    private String SHA(String str) {
        if (str != null && str.length() > 0) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
                messageDigest.update(str.getBytes());
                byte[] bArrDigest = messageDigest.digest();
                StringBuffer stringBuffer = new StringBuffer();
                for (byte b2 : bArrDigest) {
                    String hexString = Integer.toHexString(b2 & 255);
                    if (hexString.length() == 1) {
                        stringBuffer.append('0');
                    }
                    stringBuffer.append(hexString);
                }
                return stringBuffer.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String encrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, new SecretKeySpec(this.key.getBytes(), "AES"));
            return Base64.encodeToString(cipher.doFinal(str.getBytes()), 2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String str) {
        try {
            byte[] bArrDecode = Base64.decode(str, 2);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, new SecretKeySpec(this.key.getBytes(), "AES"));
            return new String(cipher.doFinal(bArrDecode));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
