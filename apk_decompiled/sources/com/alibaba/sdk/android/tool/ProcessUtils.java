package com.alibaba.sdk.android.tool;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import java.lang.reflect.Method;
import java.util.List;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class ProcessUtils {
    private static String a(Context context) {
        try {
            Method declaredMethod = Class.forName("android.app.ActivityThread", false, context.getClassLoader()).getDeclaredMethod("currentProcessName", new Class[0]);
            declaredMethod.setAccessible(true);
            return (String) declaredMethod.invoke(null, new Object[0]);
        } catch (Exception e) {
            b.a("Utils", "getProcessNameByActivityThread error: " + e);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:35:0x006d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r6v1, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String b(android.content.Context r6) throws java.lang.Throwable {
        /*
            int r6 = android.os.Process.myPid()
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r2.<init>()     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.String r3 = "/proc/"
            r2.append(r3)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r2.append(r6)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.String r6 = "/cmdline"
            r2.append(r6)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.String r6 = r2.toString()     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r1.<init>(r6)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            boolean r6 = r1.exists()     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            if (r6 == 0) goto L3b
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.io.FileReader r2 = new java.io.FileReader     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r6.<init>(r2)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.String r1 = r6.readLine()     // Catch: java.lang.Exception -> L39 java.lang.Throwable -> L67
            java.lang.String r0 = r1.trim()     // Catch: java.lang.Exception -> L39 java.lang.Throwable -> L67
            goto L3c
        L39:
            r1 = move-exception
            goto L4b
        L3b:
            r6 = r0
        L3c:
            if (r6 == 0) goto L66
            r6.close()     // Catch: java.io.IOException -> L42
            goto L66
        L42:
            r6 = move-exception
            r6.printStackTrace()
            goto L66
        L47:
            r6 = move-exception
            goto L6b
        L49:
            r1 = move-exception
            r6 = r0
        L4b:
            java.lang.String r2 = "Utils"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L67
            r3.<init>()     // Catch: java.lang.Throwable -> L67
            java.lang.String r4 = "getProcessNameByPid error: "
            r3.append(r4)     // Catch: java.lang.Throwable -> L67
            r3.append(r1)     // Catch: java.lang.Throwable -> L67
            java.lang.String r1 = r3.toString()     // Catch: java.lang.Throwable -> L67
            com.alibaba.sdk.android.tool.b.a(r2, r1)     // Catch: java.lang.Throwable -> L67
            if (r6 == 0) goto L66
            r6.close()     // Catch: java.io.IOException -> L42
        L66:
            return r0
        L67:
            r0 = move-exception
            r5 = r0
            r0 = r6
            r6 = r5
        L6b:
            if (r0 == 0) goto L75
            r0.close()     // Catch: java.io.IOException -> L71
            goto L75
        L71:
            r0 = move-exception
            r0.printStackTrace()
        L75:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.tool.ProcessUtils.b(android.content.Context):java.lang.String");
    }

    private static String c(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
        if (activityManager == null || (runningAppProcesses = activityManager.getRunningAppProcesses()) == null) {
            return "";
        }
        int iMyPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.pid == iMyPid) {
                return runningAppProcessInfo.processName;
            }
        }
        return "";
    }

    public static String getProcessName(Context context) throws Throwable {
        if (Build.VERSION.SDK_INT >= 28) {
            return Application.getProcessName();
        }
        String strA = a(context);
        if (!TextUtils.isEmpty(strA)) {
            return strA;
        }
        String strB = b(context);
        return !TextUtils.isEmpty(strB) ? strB : c(context);
    }

    public static boolean isMainProcess(Context context) throws Throwable {
        if (context == null) {
            return false;
        }
        return context.getPackageName().equalsIgnoreCase(getProcessName(context));
    }
}
