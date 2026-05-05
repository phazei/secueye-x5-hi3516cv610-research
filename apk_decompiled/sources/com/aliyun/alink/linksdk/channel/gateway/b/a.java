package com.aliyun.alink.linksdk.channel.gateway.b;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import java.security.MessageDigest;

/* JADX INFO: compiled from: SignUtils.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {
    public static String a(String str, String str2) {
        ALog.d("SignUtils", "sign() called with: signString = [" + str + "], algorithm = [" + str2 + "]");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(str2);
            messageDigest.update(str.getBytes("UTF-8"));
            return a(messageDigest.digest());
        } catch (Exception e) {
            ALog.e("SignUtils", "hmacSign error, e" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private static final String a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length);
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() < 2) {
                stringBuffer.append(0);
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString();
    }
}
