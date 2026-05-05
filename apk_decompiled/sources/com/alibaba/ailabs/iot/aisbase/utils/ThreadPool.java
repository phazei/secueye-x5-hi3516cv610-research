package com.alibaba.ailabs.iot.aisbase.utils;

import com.alibaba.ailabs.tg.utils.LogUtils;
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

/* JADX INFO: loaded from: classes.dex */
public class ThreadPool {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static ExecutorService f2686a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static ScheduledThreadPoolExecutor f2687b;

    public static class DefaultThreadFactory implements ThreadFactory {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final ThreadGroup f2689b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final String f2691d;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final AtomicInteger f2688a = new AtomicInteger(1);

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final AtomicInteger f2690c = new AtomicInteger(1);

        public DefaultThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.f2689b = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.f2691d = "AIoTShared-" + this.f2688a.getAndIncrement() + "-t-";
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.f2689b, runnable, this.f2691d + this.f2690c.getAndIncrement());
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            return thread;
        }
    }

    public static void a() {
        int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
        LogUtils.d("[aiot]ThreadPool", "ThreadPoolcore maybe <0 which will cause crash in specific platform: " + iAvailableProcessors);
        if (iAvailableProcessors <= 0) {
            iAvailableProcessors = 1;
        }
        int iMax = Math.max(4, iAvailableProcessors);
        int iMin = Math.min(10, iAvailableProcessors * 2);
        if (iMin < iMax) {
            iMax = iMin;
        }
        LogUtils.d("[aiot]ThreadPool", "ThreadPool Start a ThreadPool with scale between " + iMax + " -> " + iMin + "and core:" + iAvailableProcessors);
        f2686a = new ThreadPoolExecutor(iMax, iMin, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DefaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        f2687b = new ScheduledThreadPoolExecutor(iMax, new DefaultThreadFactory());
    }

    public static void execute(Runnable runnable) {
        if (f2686a == null) {
            a();
        }
        f2686a.execute(runnable);
    }

    public static <T> Future<T> schedule(Callable<T> callable, long j, TimeUnit timeUnit) {
        if (f2687b == null) {
            a();
        }
        return f2687b.schedule(callable, j, timeUnit);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        if (f2687b == null) {
            a();
        }
        return f2687b.scheduleAtFixedRate(runnable, j, j2, timeUnit);
    }

    public static Future<?> submit(Runnable runnable) {
        if (f2686a == null) {
            a();
        }
        return f2686a.submit(runnable);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        if (f2686a == null) {
            a();
        }
        return f2686a.submit(callable);
    }
}
