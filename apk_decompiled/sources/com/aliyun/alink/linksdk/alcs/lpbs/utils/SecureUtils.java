package com.aliyun.alink.linksdk.alcs.lpbs.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes2.dex */
public class SecureUtils {
    public static byte[] getHMacSha1Str(String str, String str2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            return mac.doFinal(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException unused) {
            return null;
        }
    }
}
