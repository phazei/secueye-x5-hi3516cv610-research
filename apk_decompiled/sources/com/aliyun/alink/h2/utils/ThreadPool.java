package com.aliyun.alink.h2.utils;

import com.aliyun.alink.h2.b.a;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes2.dex */
public class ThreadPool {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static ExecutorService f3942a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static ScheduledThreadPoolExecutor f3943b;

    private static void a() {
        int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
        a.a("ThreadPool", "ThreadPoolcore maybe <0 which will cause crash in specific platform: " + iAvailableProcessors);
        if (iAvailableProcessors <= 0) {
            iAvailableProcessors = 1;
        }
        int iMax = Math.max(4, iAvailableProcessors);
        int iMin = Math.min(10, iAvailableProcessors * 2);
        if (iMin < iMax) {
            iMax = iMin;
        }
        a.a("ThreadPool", "ThreadPool Start a ThreadPool with scale between " + iMax + " -> " + iMin + "and core:" + iAvailableProcessors);
        f3942a = new ThreadPoolExecutor(iMax, iMin, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DefaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        f3943b = new ScheduledThreadPoolExecutor(iMax, new DefaultThreadFactory());
    }

    public static void execute(Runnable runnable) {
        if (f3942a == null) {
            a();
        }
        f3942a.execute(runnable);
    }

    public static Future<?> submit(Runnable runnable) {
        if (f3942a == null) {
            a();
        }
        return f3942a.submit(runnable);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        if (f3942a == null) {
            a();
        }
        return f3942a.submit(callable);
    }

    public static <T> Future<T> schedule(Callable<T> callable, long j, TimeUnit timeUnit) {
        if (f3943b == null) {
            a();
        }
        return f3943b.schedule(callable, j, timeUnit);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        if (f3943b == null) {
            a();
        }
        return f3943b.scheduleAtFixedRate(runnable, j, j2, timeUnit);
    }

    public static class DefaultThreadFactory implements ThreadFactory {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final ThreadGroup f3945b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final String f3947d;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final AtomicInteger f3944a = new AtomicInteger(1);

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final AtomicInteger f3946c = new AtomicInteger(1);

        public DefaultThreadFactory() {
            ThreadGroup threadGroup;
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                threadGroup = securityManager.getThreadGroup();
            } else {
                threadGroup = Thread.currentThread().getThreadGroup();
            }
            this.f3945b = threadGroup;
            this.f3947d = "Shared-" + this.f3944a.getAndIncrement() + "-t-";
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.f3945b, runnable, this.f3947d + this.f3946c.getAndIncrement());
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            return thread;
        }
    }
}
