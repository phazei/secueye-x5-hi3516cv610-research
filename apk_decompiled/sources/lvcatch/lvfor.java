package lvcatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.Deflater;

/* JADX INFO: loaded from: classes4.dex */
public class lvfor {
    public static String lvdo() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String lvdo(String str, String str2, String str3) {
        try {
            return "LOG " + str + ":" + new lvgoto.lvfor().lvdo(str2, str3).trim();
        } catch (Exception e) {
            throw new IllegalStateException("Compute signature failed!", e);
        }
    }

    public static void lvdo(boolean z, String str) {
        if (!z) {
            throw new IllegalArgumentException(str);
        }
    }

    public static byte[] lvdo(byte[] bArr) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream;
        Deflater deflater = new Deflater();
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
            } catch (Exception unused) {
            }
        } catch (Throwable th) {
            th = th;
            byteArrayOutputStream = byteArrayOutputStream2;
        }
        try {
            deflater.setInput(bArr);
            deflater.finish();
            byte[] bArr2 = new byte[10240];
            while (!deflater.finished()) {
                byteArrayOutputStream.write(bArr2, 0, deflater.deflate(bArr2));
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            deflater.end();
            try {
                if (byteArrayOutputStream.size() != 0) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException unused2) {
            }
            return byteArray;
        } catch (Exception unused3) {
            byteArrayOutputStream2 = byteArrayOutputStream;
            throw new lvcase.lvfor(-1, "fail to zip data", "");
        } catch (Throwable th2) {
            th = th2;
            deflater.end();
            try {
                if (byteArrayOutputStream.size() != 0) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException unused4) {
            }
            throw th;
        }
    }

    public static String lvif(byte[] bArr) throws lvcase.lvfor {
        try {
            String upperCase = new BigInteger(1, MessageDigest.getInstance("MD5").digest(bArr)).toString(16).toUpperCase();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; upperCase.length() + i < 32; i++) {
                sb.append("0");
            }
            return sb.toString() + upperCase;
        } catch (NoSuchAlgorithmException e) {
            throw new lvcase.lvfor(-1, "Not Supported signature method MD5", e, "");
        }
    }
}
