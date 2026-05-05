package com.alibaba.sdk.android.utils;

import android.util.Log;
import java.lang.Thread;

/* JADX INFO: compiled from: AlicloudUncaughtExceptionHandler.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements Thread.UncaughtExceptionHandler {
    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        Log.e("AlicloudUtils", "Catch an uncaught exception, " + thread.getName() + ", error message: " + th.getMessage());
        th.printStackTrace();
    }
}
