package com.aliyun.iot.aep.sdk.framework.utils;

import android.os.Handler;
import android.os.Looper;

/* JADX INFO: loaded from: classes2.dex */
public class ThreadUtils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Handler f4739a;

    public static void retryInMain(Runnable runnable, long j) {
        if (f4739a == null) {
            f4739a = new Handler(Looper.getMainLooper());
        }
        try {
            f4739a.removeCallbacks(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            f4739a.postDelayed(runnable, j);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void remove(Runnable runnable) {
        if (f4739a == null) {
            f4739a = new Handler(Looper.getMainLooper());
        }
        try {
            f4739a.removeCallbacks(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
