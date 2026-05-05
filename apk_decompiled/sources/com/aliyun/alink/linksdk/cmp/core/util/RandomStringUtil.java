package com.aliyun.alink.linksdk.cmp.core.util;

import java.util.Random;

/* JADX INFO: loaded from: classes2.dex */
public class RandomStringUtil {
    public static String getRandomString(int i) {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt(62)));
        }
        return stringBuffer.toString();
    }
}
