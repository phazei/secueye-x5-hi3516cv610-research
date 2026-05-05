package com.alibaba.cloudapi.sdk.signature;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SignerFactoryManager {
    private static Map<String, ISignerFactory> factoryMap = new HashMap(2);

    public static void init() {
        registerSignerFactory("HmacSHA256", new HMacSHA256SignerFactory());
        registerSignerFactory("HmacSHA1", new HMacSHA1SignerFactory());
    }

    public static ISignerFactory registerSignerFactory(String str, ISignerFactory iSignerFactory) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("method can not be empty");
        }
        if (iSignerFactory == null) {
            throw new IllegalArgumentException("factory can not be null");
        }
        return factoryMap.put(str, iSignerFactory);
    }

    public static ISignerFactory findSignerFactory(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "HmacSHA256";
        }
        return factoryMap.get(str);
    }
}
