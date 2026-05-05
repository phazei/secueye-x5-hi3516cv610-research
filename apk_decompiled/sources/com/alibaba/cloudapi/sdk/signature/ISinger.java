package com.alibaba.cloudapi.sdk.signature;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes.dex */
public interface ISinger {
    String sign(String str, String str2) throws NoSuchAlgorithmException, InvalidKeyException;
}
