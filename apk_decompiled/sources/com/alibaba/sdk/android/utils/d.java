package com.alibaba.sdk.android.utils;

import android.util.Log;

/* JADX INFO: compiled from: Logger.java */
/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static boolean f3229c = false;

    public static boolean c() {
        return f3229c;
    }

    public static void setLogEnabled(boolean z) {
        f3229c = z;
    }

    public static void a(String str, String str2) {
        if (f3229c) {
            Log.d(str, str2);
        }
    }

    public static void b(String str, String str2) {
        if (f3229c) {
            Log.i(str, str2);
        }
    }

    public static void c(String str, String str2) {
        if (f3229c) {
            Log.e(str, str2);
        }
    }

    public static void a(String str, Throwable th) {
        if (!f3229c || th == null) {
            return;
        }
        Log.e(str, th.toString(), th);
    }
}
