package anet.channel.strategy.utils;

import anet.channel.util.ALog;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AtomicInteger f1922a = new AtomicInteger(0);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static ScheduledThreadPoolExecutor f1923b = null;

    static ScheduledThreadPoolExecutor a() {
        if (f1923b == null) {
            synchronized (a.class) {
                if (f1923b == null) {
                    f1923b = new ScheduledThreadPoolExecutor(2, new b());
                    f1923b.setKeepAliveTime(60L, TimeUnit.SECONDS);
                    f1923b.allowCoreThreadTimeOut(true);
                }
            }
        }
        return f1923b;
    }

    public static void a(Runnable runnable) {
        try {
            a().submit(runnable);
        } catch (Exception e) {
            ALog.e(anet.channel.strategy.dispatch.a.TAG, "submit task failed", null, e, new Object[0]);
        }
    }

    public static void a(Runnable runnable, long j) {
        try {
            a().schedule(runnable, j, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            ALog.e(anet.channel.strategy.dispatch.a.TAG, "schedule task failed", null, e, new Object[0]);
        }
    }
}
