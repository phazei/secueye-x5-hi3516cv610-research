package com.linkkit.tools.utils;

import java.text.SimpleDateFormat;

/* JADX INFO: loaded from: classes3.dex */
public class SystemUtils {
    public static String getCurrentTime(long j) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(j));
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(j);
        }
    }
}
