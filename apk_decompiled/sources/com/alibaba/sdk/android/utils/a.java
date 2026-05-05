package com.alibaba.sdk.android.utils;

import java.util.concurrent.ThreadFactory;

/* JADX INFO: compiled from: AlicloudThreadFactory.java */
/* JADX INFO: loaded from: classes.dex */
public class a implements ThreadFactory {
    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setUncaughtExceptionHandler(new b());
        return thread;
    }
}
