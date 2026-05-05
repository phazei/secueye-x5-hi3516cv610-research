package com.alibaba.sdk.android.tool;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static boolean f3205a = false;

    public static void a(String str, String str2) {
        if (f3205a) {
            Log.d(str, str2);
        }
    }

    public static void b(String str, String str2) {
        if (f3205a) {
            Log.e(str, str2);
        }
    }
}
