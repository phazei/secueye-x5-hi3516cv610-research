package com.aliyun.alink.h2.api;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

/* JADX INFO: loaded from: classes2.dex */
public enum AuthSignMethod {
    HMACMD5("HMACMD5"),
    SHA1("SHA1"),
    SHA256("SHA256"),
    SHA384("SHA384"),
    SHA512("SHA512");

    private String method;

    AuthSignMethod(String str) {
        this.method = null;
        this.method = str;
    }

    public String getMethod() {
        return this.method;
    }

    public static String sign(AuthSignMethod authSignMethod, String str, String str2) {
        HmacAlgorithms hmacAlgorithms = HmacAlgorithms.HMAC_SHA_256;
        if (authSignMethod == HMACMD5) {
            hmacAlgorithms = HmacAlgorithms.HMAC_MD5;
        } else if (authSignMethod == SHA1) {
            hmacAlgorithms = HmacAlgorithms.HMAC_SHA_1;
        } else if (authSignMethod == SHA384) {
            hmacAlgorithms = HmacAlgorithms.HMAC_SHA_384;
        } else if (authSignMethod == SHA512) {
            hmacAlgorithms = HmacAlgorithms.HMAC_SHA_512;
        }
        return new String(Hex.encodeHex(new HmacUtils(hmacAlgorithms, str2).hmac(str)));
    }
}
