package com.alibaba.sdk.android.push.common.util;

import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class ThreadPoolFactory {
    private static volatile ScheduledThreadPoolExecutor scheduleThreadPoolExecutor;
    private static final AtomicInteger integer = new AtomicInteger();
    private static final String TAG = "MPS:ThreadPoolFactory";
    private static AmsLogger logger = AmsLogger.getLogger(TAG);

    static class a implements ThreadFactory {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f3053a;

        public a(String str) {
            this.f3053a = str;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, this.f3053a + ThreadPoolFactory.integer.getAndIncrement());
            thread.setPriority(5);
            return thread;
        }
    }

    public static void execute(Runnable runnable) {
        try {
            getScheduledExecutor().execute(runnable);
        } catch (Throwable th) {
            logger.e("ThreadPoolExecutorFactory execute", th);
        }
    }

    public static ScheduledThreadPoolExecutor getScheduledExecutor() {
        if (scheduleThreadPoolExecutor == null) {
            synchronized (ThreadPoolFactory.class) {
                if (scheduleThreadPoolExecutor == null) {
                    scheduleThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new a(TAG));
                    scheduleThreadPoolExecutor.setKeepAliveTime(60L, TimeUnit.SECONDS);
                    scheduleThreadPoolExecutor.allowCoreThreadTimeOut(true);
                }
            }
        }
        return scheduleThreadPoolExecutor;
    }

    public static ScheduledFuture<?> schedule(Runnable runnable, long j, TimeUnit timeUnit) {
        try {
            return getScheduledExecutor().schedule(runnable, j, timeUnit);
        } catch (Throwable th) {
            logger.e("ThreadPoolExecutorFactory schedule", th);
            return null;
        }
    }
}
