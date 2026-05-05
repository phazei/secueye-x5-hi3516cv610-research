package com.alibaba.ailabs.tg.thread;

import android.os.Handler;
import android.os.Looper;

/* JADX INFO: loaded from: classes.dex */
public final class MainTaskHandler {
    private static volatile Handler sHandler;

    private static Handler get() {
        if (sHandler == null) {
            synchronized (MainTaskHandler.class) {
                if (sHandler == null) {
                    sHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sHandler;
    }

    public static void post(Runnable runnable) {
        get().post(runnable);
    }

    public static void postDelay(Runnable runnable, long j) {
        get().postDelayed(runnable, j);
    }
}
