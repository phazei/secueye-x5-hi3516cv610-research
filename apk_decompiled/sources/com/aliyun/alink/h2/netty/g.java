package com.aliyun.alink.h2.netty;

import java.lang.Thread;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: compiled from: ThreadFactoryBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f3894a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Boolean f3895b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Integer f3896c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Thread.UncaughtExceptionHandler f3897d = null;
    private ThreadFactory e = null;

    public g a(String str) {
        b(str, 0);
        this.f3894a = str;
        return this;
    }

    public g a(boolean z) {
        this.f3895b = Boolean.valueOf(z);
        return this;
    }

    public ThreadFactory a() {
        return a(this);
    }

    private static ThreadFactory a(g gVar) {
        final String str = gVar.f3894a;
        final Boolean bool = gVar.f3895b;
        final Integer num = gVar.f3896c;
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = gVar.f3897d;
        ThreadFactory threadFactoryDefaultThreadFactory = gVar.e;
        if (threadFactoryDefaultThreadFactory == null) {
            threadFactoryDefaultThreadFactory = Executors.defaultThreadFactory();
        }
        final ThreadFactory threadFactory = threadFactoryDefaultThreadFactory;
        final AtomicLong atomicLong = str != null ? new AtomicLong(0L) : null;
        return new ThreadFactory() { // from class: com.aliyun.alink.h2.netty.g.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                Thread threadNewThread = threadFactory.newThread(runnable);
                String str2 = str;
                if (str2 != null) {
                    threadNewThread.setName(g.b(str2, Long.valueOf(atomicLong.getAndIncrement())));
                }
                Boolean bool2 = bool;
                if (bool2 != null) {
                    threadNewThread.setDaemon(bool2.booleanValue());
                }
                Integer num2 = num;
                if (num2 != null) {
                    threadNewThread.setPriority(num2.intValue());
                }
                Thread.UncaughtExceptionHandler uncaughtExceptionHandler2 = uncaughtExceptionHandler;
                if (uncaughtExceptionHandler2 != null) {
                    threadNewThread.setUncaughtExceptionHandler(uncaughtExceptionHandler2);
                }
                return threadNewThread;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(String str, Object... objArr) {
        return String.format(Locale.ROOT, str, objArr);
    }
}
