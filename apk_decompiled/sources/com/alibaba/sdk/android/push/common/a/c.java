package com.alibaba.sdk.android.push.common.a;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class c {
    public static synchronized void a(int i) {
        b.f3048d = i;
    }

    public static synchronized void a(Bitmap bitmap) {
        b.f3046b = bitmap;
    }

    public static synchronized void a(String str) {
        if (str != null) {
            if (str.length() > 0) {
                b.f3045a = str;
            }
        }
    }

    public static synchronized void a(boolean z) {
        b.g = z;
    }
}
