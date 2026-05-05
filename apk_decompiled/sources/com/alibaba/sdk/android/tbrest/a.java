package com.alibaba.sdk.android.tbrest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: SendAsyncExecutor.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static ScheduledExecutorService f3184a = null;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    public static final AtomicInteger f17a = new AtomicInteger();
    public static int g = 1;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    public Integer f18a = 2;

    /* JADX INFO: renamed from: com.alibaba.sdk.android.tbrest.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: SendAsyncExecutor.java */
    static class ThreadFactoryC0204a implements ThreadFactory {
        private final int priority;

        public ThreadFactoryC0204a(int i) {
            this.priority = i;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "RestSend:" + a.f17a.getAndIncrement());
            thread.setPriority(this.priority);
            return thread;
        }
    }

    public synchronized void a(Runnable runnable) {
        try {
            if (f3184a == null) {
                f3184a = Executors.newScheduledThreadPool(this.f18a.intValue(), new ThreadFactoryC0204a(g));
            }
            f3184a.submit(runnable);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
