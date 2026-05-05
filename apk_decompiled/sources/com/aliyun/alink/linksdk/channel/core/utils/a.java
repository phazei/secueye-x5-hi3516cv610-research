package com.aliyun.alink.linksdk.channel.core.utils;

import com.aliyun.alink.linksdk.tools.log.HLoggerFactory;
import com.aliyun.alink.linksdk.tools.log.ILogger;

/* JADX INFO: compiled from: ALog.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static ILogger f4149a = new HLoggerFactory().getInstance("LK-core-");

    public static void a(String str, String str2) {
        f4149a.d(str, str2);
    }

    public static void b(String str, String str2) {
        f4149a.e(str, str2);
    }

    public static void c(String str, String str2) {
        f4149a.i(str, str2);
    }

    public static void d(String str, String str2) {
        f4149a.w(str, str2);
    }
}
