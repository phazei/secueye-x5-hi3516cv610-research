package com.aliyun.alink.h2.utils;

/* JADX INFO: loaded from: classes2.dex */
public class StringUtil {
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return !isEmpty(charSequence);
    }
}
