package com.alibaba.sdk.android.ams.common.a;

import android.app.Application;
import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public static void a(Application application) {
        if (application == null) {
            return;
        }
        a.f2829b = application;
    }

    public static void a(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("null applicationContext!");
        }
        a.f2828a = context;
    }

    public static void a(String str) {
        a.f2831d = str;
    }

    public static void a(boolean z) {
        a.f2830c = z;
    }

    public static void b(String str) {
        a.e = str;
    }

    public static void c(String str) {
        a.f = str;
    }

    public static void d(String str) {
        a.g = str;
    }

    public static void e(String str) {
        a.h = str;
    }
}
