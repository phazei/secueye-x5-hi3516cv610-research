package com.alibaba.sdk.android.beacon;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static boolean f2845b;

    public static void a(String str, String str2) {
        if (f2845b) {
            Log.i(str, str2);
        }
    }

    public static void a(String str, String str2, Throwable th) {
        if (f2845b) {
            Log.e(str, str2, th);
        }
    }

    public static void setLogEnabled(boolean z) {
        f2845b = z;
    }
}
