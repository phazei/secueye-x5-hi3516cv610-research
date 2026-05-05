package a.a.a.a.a.a.a;

import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.log.HLoggerFactory;
import com.aliyun.alink.linksdk.tools.log.ILogger;

/* JADX INFO: compiled from: ALog.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static ILogger f1109a = new HLoggerFactory().getInstance("LK-mc-");

    public static String a(String str) {
        return str;
    }

    public static void a(String str, String str2) {
        f1109a.d(str, a(str2));
    }

    public static void b(String str, String str2) {
        f1109a.e(str, a(str2));
    }

    public static void c(String str, String str2) {
        f1109a.i(str, a(str2));
    }

    public static void d(String str, String str2) {
        f1109a.w(str, a(str2));
    }

    public static void a(int i) {
        ALog.setLevel((byte) i);
    }
}
