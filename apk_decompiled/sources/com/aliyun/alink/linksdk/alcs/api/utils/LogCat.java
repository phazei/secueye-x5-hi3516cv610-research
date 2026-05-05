package com.aliyun.alink.linksdk.alcs.api.utils;

import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class LogCat {
    public static void e(String str, String str2, Throwable th) {
        ALog.e(str, str2, th == null ? null : th.toString());
    }

    public static void e(String str, String str2) {
        ALog.e(str, str2);
    }

    public static void d(String str, String str2, Throwable th) {
        ALog.d(str, str2);
    }

    public static void d(String str, String str2) {
        ALog.d(str, str2);
    }

    public static void i(String str, String str2, Throwable th) {
        ALog.i(str, str2);
    }

    public static void i(String str, String str2) {
        ALog.i(str, str2);
    }

    public static void v(String str, String str2, Throwable th) {
        i(str, str2, th);
    }

    public static void v(String str, String str2) {
        i(str, str2);
    }

    public static void w(String str, String str2, Throwable th) {
        ALog.w(str, str2);
    }

    public static void w(String str, String str2) {
        ALog.w(str, str2);
    }
}
