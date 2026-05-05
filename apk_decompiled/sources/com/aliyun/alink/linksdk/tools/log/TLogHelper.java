package com.aliyun.alink.linksdk.tools.log;

import android.util.Log;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes2.dex */
public class TLogHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile boolean f4455a = true;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Class f4456b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Method f4457c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static Method f4458d;
    private static Method e;
    private static Method f;
    private static Method g;

    static {
        try {
            if (f4456b == null) {
                f4456b = Class.forName("com.taobao.tao.log.TLog");
                f4457c = f4456b.getDeclaredMethod("logv", String.class, String.class, String.class);
                f4458d = f4456b.getDeclaredMethod("logd", String.class, String.class, String.class);
                e = f4456b.getDeclaredMethod("logi", String.class, String.class, String.class);
                f = f4456b.getDeclaredMethod("logw", String.class, String.class, String.class);
                g = f4456b.getDeclaredMethod("loge", String.class, String.class, String.class);
            }
        } catch (Exception e2) {
            Log.e("TLogHelper", "printToTLog reflect e:" + e2.toString());
        }
    }

    public static void setToTlogOn(boolean z) {
        Log.d("TLogHelper", "setToTlogOn on:" + z);
        f4455a = z;
    }

    public static boolean isToTlogOn() {
        return f4455a;
    }

    public static void printToTLog(int i, String str, String str2) {
        Method method;
        if (isToTlogOn()) {
            if (i <= 2) {
                Method method2 = f4457c;
                if (method2 == null) {
                    return;
                }
                try {
                    method2.invoke(f4456b, "", str, str2);
                    return;
                } catch (Exception e2) {
                    Log.e("TLogHelper", "printToTLog logvMethodOfTLog e:" + e2.toString());
                    return;
                }
            }
            if (i == 3) {
                Method method3 = f4458d;
                if (method3 == null) {
                    return;
                }
                try {
                    method3.invoke(f4456b, "", str, str2);
                    return;
                } catch (Exception e3) {
                    Log.e("TLogHelper", "printToTLog logvMethodOfTLog e:" + e3.toString());
                    return;
                }
            }
            if (i == 4) {
                Method method4 = e;
                if (method4 == null) {
                    return;
                }
                try {
                    method4.invoke(f4456b, "", str, str2);
                    return;
                } catch (Exception e4) {
                    Log.e("TLogHelper", "printToTLog logvMethodOfTLog e:" + e4.toString());
                    return;
                }
            }
            if (i == 5) {
                Method method5 = f;
                if (method5 == null) {
                    return;
                }
                try {
                    method5.invoke(f4456b, "", str, str2);
                    return;
                } catch (Exception e5) {
                    Log.e("TLogHelper", "printToTLog logvMethodOfTLog e:" + e5.toString());
                    return;
                }
            }
            if (i < 6 || (method = g) == null) {
                return;
            }
            try {
                method.invoke(f4456b, "", str, str2);
            } catch (Exception e6) {
                Log.e("TLogHelper", "printToTLog logvMethodOfTLog e:" + e6.toString());
            }
        }
    }
}
