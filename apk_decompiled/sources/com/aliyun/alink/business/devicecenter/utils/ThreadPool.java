package com.aliyun.alink.business.devicecenter.utils;

import com.aliyun.alink.business.devicecenter.log.ALog;
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
    public static ExecutorService f3778a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static ScheduledThreadPoolExecutor f3779b;

    public static class DefaultThreadFactory implements ThreadFactory {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final ThreadGroup f3781b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final String f3783d;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final AtomicInteger f3780a = new AtomicInteger(1);

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final AtomicInteger f3782c = new AtomicInteger(1);

        public DefaultThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.f3781b = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.f3783d = "Shared-" + this.f3780a.getAndIncrement() + "-t-";
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.f3781b, runnable, this.f3783d + this.f3782c.getAndIncrement());
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            return thread;
        }
    }

    public static void a() {
        int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
        ALog.d("ThreadPool", "core maybe <0 which will cause crash in specific platform: " + iAvailableProcessors);
        if (iAvailableProcessors <= 0) {
            iAvailableProcessors = 1;
        }
        int iMax = Math.max(4, iAvailableProcessors);
        int iMin = Math.min(10, iAvailableProcessors * 2);
        if (iMin < iMax) {
            iMax = iMin;
        }
        ALog.d("ThreadPool", "Start a ThreadPool with scale between " + iMax + " -> " + iMin + "and core:" + iAvailableProcessors);
        f3778a = new ThreadPoolExecutor(iMax, iMin, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DefaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        f3779b = new ScheduledThreadPoolExecutor(iMax, new DefaultThreadFactory());
    }

    public static void execute(Runnable runnable) {
        if (f3778a == null) {
            a();
        }
        f3778a.execute(runnable);
    }

    public static <T> Future<T> schedule(Callable<T> callable, long j, TimeUnit timeUnit) {
        if (f3779b == null) {
            a();
        }
        return f3779b.schedule(callable, j, timeUnit);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        if (f3779b == null) {
            a();
        }
        return f3779b.scheduleAtFixedRate(runnable, j, j2, timeUnit);
    }

    public static Future<?> submit(Runnable runnable) {
        if (f3778a == null) {
            a();
        }
        return f3778a.submit(runnable);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        if (f3778a == null) {
            a();
        }
        return f3778a.submit(callable);
    }
}
