package com.alibaba.ailabs.tg.utils;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class LogUtils {
    public static final int DEBUGGABLE = 5;
    public static final String TAG = "tmall_genie";
    private static boolean sLogEnable = false;

    private static String getClassMsg() {
        StackTraceElement stackTraceElement = new Throwable().fillInStackTrace().getStackTrace()[2];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(stackTraceElement.getFileName());
        sb.append(" ");
        sb.append(stackTraceElement.getMethodName());
        sb.append("() :: line " + stackTraceElement.getLineNumber() + "] --> ");
        return sb.toString();
    }

    public static void enable(boolean z) {
        sLogEnable = z;
    }

    public static boolean isEnable() {
        return sLogEnable;
    }

    public static void v(String str) {
        if (sLogEnable) {
            Log.v(TAG, getClassMsg() + str);
        }
    }

    public static void v(String str, String str2) {
        if (sLogEnable) {
            Log.v(str, getClassMsg() + str2);
        }
    }

    public static void i(String str) {
        if (sLogEnable) {
            Log.i(TAG, getClassMsg() + str);
        }
    }

    public static void i(String str, String str2) {
        if (sLogEnable) {
            Log.i(str, getClassMsg() + str2);
        }
    }

    public static void d(String str) {
        if (sLogEnable) {
            Log.d(TAG, getClassMsg() + str);
        }
    }

    public static void d(String str, String str2) {
        if (sLogEnable) {
            Log.d(str, getClassMsg() + str2);
        }
    }

    public static void w(String str) {
        if (sLogEnable) {
            Log.w(TAG, getClassMsg() + str);
        }
    }

    public static void w(String str, String str2) {
        if (sLogEnable) {
            Log.w(str, getClassMsg() + str2);
        }
    }

    public static void e(String str) {
        if (sLogEnable) {
            Log.e(TAG, getClassMsg() + str);
        }
    }

    public static void e(String str, String str2) {
        if (sLogEnable) {
            Log.e(str, getClassMsg() + str2);
        }
    }
}
