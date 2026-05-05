package com.aliyun.alink.linksdk.alcs.lpbs.utils;

import java.security.SecureRandom;

/* JADX INFO: loaded from: classes2.dex */
public class RandomUtils {
    private static final String RANDOM_BYTE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    protected static SecureRandom sRandom = new SecureRandom();

    public static String getRandomString(int i) {
        StringBuilder sb = new StringBuilder(i);
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(RANDOM_BYTE.charAt(sRandom.nextInt(62)));
        }
        return sb.toString();
    }

    public static int getNextInt() {
        return sRandom.nextInt();
    }
}
