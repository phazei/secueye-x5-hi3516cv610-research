package com.alibaba.sdk.android.openaccount.util.safe;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class RSAKey {
    private static final String DEFAULT_RSA_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl4EFDk91/ArPHjyX7UBzofPTAD3pcP8FMgOs83hvLEcbFJOVASrPAjbJTuXsSZJd9tYPwKbuqlGqndvdl2Kn2zLFpLOcFAYOyaIDFzDOCWQw/kMjcm1U08BvPE7dbtkGM23lCyTBlDMHWJvUz3JVTZm6ApGWEOGRhs1rECjcS9HXttnllQ2gTtBAW5Xjb8tzDgWR0jMaHzduCcSimHPtQO4Osh4Op3ianRocbb9o/4OR8HgKdbaKO3Sq2+pYV7FveXmfXqUr5lH7oHji+4j5TaU4WXRGKOjHSVXtN0UrfCXtsWE0aGCXXQN78NJUf5VrJMh14mqiSrR07wgu3UG7OwIDAQAB";
    private static String rsaPubKey;

    public static String getRsaPubkey() {
        if (TextUtils.isEmpty(rsaPubKey)) {
            rsaPubKey = DEFAULT_RSA_KEY;
        }
        return rsaPubKey;
    }
}
