package lvgoto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes4.dex */
public class lvfor {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private static final Object f8015lvdo = new Object();

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private static Mac f8016lvif;

    private byte[] lvdo(byte[] bArr, byte[] bArr2) {
        Mac mac;
        try {
            if (f8016lvif == null) {
                synchronized (f8015lvdo) {
                    if (f8016lvif == null) {
                        f8016lvif = Mac.getInstance(lvdo());
                    }
                }
            }
            try {
                mac = (Mac) f8016lvif.clone();
            } catch (CloneNotSupportedException unused) {
                mac = Mac.getInstance(lvdo());
            }
            mac.init(new SecretKeySpec(bArr, lvdo()));
            return mac.doFinal(bArr2);
        } catch (InvalidKeyException unused2) {
            throw new RuntimeException("key must not be null");
        } catch (NoSuchAlgorithmException unused3) {
            throw new RuntimeException("Unsupported algorithm: HmacSHA1");
        }
    }

    public String lvdo() {
        return "HmacSHA1";
    }

    public String lvdo(String str, String str2) {
        lvcase.lvint.lvif(lvdo(), false);
        lvcase.lvint.lvif(lvif(), false);
        try {
            return lvcatch.lvdo.lvdo(lvdo(str.getBytes("UTF-8"), str2.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException unused) {
            throw new RuntimeException("Unsupported algorithm: UTF-8");
        }
    }

    public String lvif() {
        return "1";
    }
}
