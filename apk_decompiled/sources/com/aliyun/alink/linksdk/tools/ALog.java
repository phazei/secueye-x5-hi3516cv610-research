package com.aliyun.alink.linksdk.tools;

import android.util.Log;
import com.aliyun.alink.linksdk.tools.log.ILogDispatcher;
import com.aliyun.alink.linksdk.tools.log.TLogHelper;

/* JADX INFO: loaded from: classes2.dex */
public class ALog {
    public static final byte LEVEL_DEBUG = 1;
    public static final byte LEVEL_ERROR = 4;
    public static final byte LEVEL_INFO = 2;
    public static final byte LEVEL_WARNING = 3;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static byte f4439a = 3;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static byte f4440b = 4;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final int f4441c = 8;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static ILogDispatcher f4442d;

    public static void setLogDispatcher(ILogDispatcher iLogDispatcher) {
        f4442d = iLogDispatcher;
    }

    public static byte getUploadLevel() {
        return f4440b;
    }

    public static void setUploadLevel(byte b2) {
        Log.d("ALog", "setUploadLevel uploadLevel:" + ((int) b2));
        if (b2 < 2) {
            b2 = 2;
        }
        f4440b = b2;
    }

    public static String makeLogTag(String str) {
        if (str.length() > 36 - f4441c) {
            return "linksdk_" + str.substring(0, (36 - f4441c) - 1);
        }
        return "linksdk_" + str;
    }

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void setLevel(byte b2) {
        f4439a = b2;
    }

    public static byte getLevel() {
        return f4439a;
    }

    private static void a(int i, String str, String str2) {
        ILogDispatcher iLogDispatcher = f4442d;
        if (iLogDispatcher != null) {
            iLogDispatcher.log(i, str, str2);
        }
        if (f4439a + 2 > i) {
            return;
        }
        TLogHelper.printToTLog(i, str, str2);
        if (6 < i) {
            i = 6;
        }
        if (3 > i) {
            i = 3;
        }
        if (str == null) {
            print(i, str, str2);
        } else {
            print(i, str, str2);
        }
    }

    public static void llog(byte b2, String str, String str2) {
        if (f4439a + 2 <= b2 && str2 != null) {
            int length = str2.length();
            int i = 0;
            while (i < length - 900) {
                int i2 = i + 900;
                a(b2, str, str2.substring(i, i2));
                i = i2;
            }
            a(b2, str, str2.substring(i));
        }
    }

    public static void d(String str, String str2) {
        a(3, str, str2);
    }

    public static void i(String str, String str2) {
        a(4, str, str2);
    }

    public static void w(String str, String str2) {
        a(5, str, str2);
    }

    public static void e(String str, String str2) {
        a(6, str, str2);
    }

    public static void e(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        if (str2 == null) {
            str2 = "";
        }
        sb.append(str2);
        sb.append(" ERROR: ");
        sb.append(str3);
        a(6, str, sb.toString());
    }

    public static void e(String str, String str2, Exception exc) {
        if (exc != null) {
            StringBuilder sb = new StringBuilder();
            if (str2 == null) {
                str2 = "";
            }
            sb.append(str2);
            sb.append(" EXCEPTION: ");
            sb.append(exc.getMessage());
            a(6, str, sb.toString());
            exc.printStackTrace();
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        if (str2 == null) {
            str2 = "";
        }
        sb2.append(str2);
        sb2.append(" EXCEPTION: unknown");
        a(6, str, sb2.toString());
    }

    public static void logBA(byte b2, String str, byte[] bArr) {
        ILogDispatcher iLogDispatcher;
        if (f4439a + 2 <= b2 && (iLogDispatcher = f4442d) != null) {
            iLogDispatcher.log(b2, str, bArr);
        }
    }

    public static void print(int i, String str, String str2) {
        if (str2 == null || "".equals(str2)) {
            return;
        }
        Log.println(i, str, str2);
    }
}
