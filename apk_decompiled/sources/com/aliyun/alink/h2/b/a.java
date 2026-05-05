package com.aliyun.alink.h2.b;

import com.aliyun.alink.h2.utils.HLoggerFactory;
import com.aliyun.alink.h2.utils.ILogger;

/* JADX INFO: compiled from: ALog.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static ILogger f3818a = new HLoggerFactory().getInstance("H2Base-");

    public static void a(String str, String str2) {
        f3818a.d(str, str2);
    }

    public static void b(String str, String str2) {
        f3818a.i(str, str2);
    }

    public static void c(String str, String str2) {
        f3818a.w(str, str2);
    }

    public static void d(String str, String str2) {
        f3818a.e(str, str2);
    }

    public static void a(String str, String str2, Exception exc) {
        if (exc != null) {
            ILogger iLogger = f3818a;
            StringBuilder sb = new StringBuilder();
            if (str2 == null) {
                str2 = "";
            }
            sb.append(str2);
            sb.append(" EXCEPTION: ");
            sb.append(exc.getMessage());
            iLogger.e(str, sb.toString());
            exc.printStackTrace();
            return;
        }
        ILogger iLogger2 = f3818a;
        StringBuilder sb2 = new StringBuilder();
        if (str2 == null) {
            str2 = "";
        }
        sb2.append(str2);
        sb2.append(" EXCEPTION: unknown");
        iLogger2.e(str, sb2.toString());
    }
}
