package com.alibaba.sdk.android.crashdefend.b;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ThreadFactory f2867a = new ThreadFactory() { // from class: com.alibaba.sdk.android.crashdefend.b.a.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "safe_thread");
            thread.setDaemon(false);
            return thread;
        }
    };

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ExecutorService f2868b;

    public synchronized ExecutorService a() {
        if (this.f2868b == null) {
            this.f2868b = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 1L, TimeUnit.SECONDS, new SynchronousQueue(), this.f2867a);
        }
        return this.f2868b;
    }
}
