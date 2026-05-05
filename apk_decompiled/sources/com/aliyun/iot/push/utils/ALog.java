package com.aliyun.iot.push.utils;

import com.aliyun.alink.linksdk.tools.log.HLoggerFactory;
import com.aliyun.alink.linksdk.tools.log.ILogger;

/* JADX INFO: loaded from: classes2.dex */
public class ALog {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static ILogger f4956a = new HLoggerFactory().getInstance("Push-");

    public static void d(String str, String str2) {
        f4956a.d(str, str2);
    }

    public static void i(String str, String str2) {
        f4956a.i(str, str2);
    }

    public static void w(String str, String str2) {
        f4956a.w(str, str2);
    }

    public static void e(String str, String str2) {
        f4956a.e(str, str2);
    }

    public static void e(String str, String str2, Throwable th) {
        if (th != null) {
            f4956a.e(str, str2 + " EXCEPTION: " + th.getMessage());
            th.printStackTrace();
            return;
        }
        f4956a.e(str, str2 + " EXCEPTION: unknown");
    }

    public static void llog(byte b2, String str, String str2) {
        com.aliyun.alink.linksdk.tools.ALog.llog(b2, "Push-" + str, str2);
    }

    public static void setLevel(byte b2) {
        com.aliyun.alink.linksdk.tools.ALog.setLevel(b2);
    }

    public static byte getLevel() {
        return com.aliyun.alink.linksdk.tools.ALog.getLevel();
    }
}
