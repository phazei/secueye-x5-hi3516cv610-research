package com.aliyun.alink.h2.stream.c;

import com.aliyun.alink.h2.utils.HLoggerFactory;
import com.aliyun.alink.h2.utils.ILogger;

/* JADX INFO: compiled from: ALog.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static ILogger f3940a = new HLoggerFactory().getInstance("H2Stream-");

    public static void a(String str, String str2) {
        f3940a.d(str, str2);
    }

    public static void b(String str, String str2) {
        f3940a.i(str, str2);
    }

    public static void c(String str, String str2) {
        f3940a.w(str, str2);
    }

    public static void d(String str, String str2) {
        f3940a.e(str, str2);
    }
}
