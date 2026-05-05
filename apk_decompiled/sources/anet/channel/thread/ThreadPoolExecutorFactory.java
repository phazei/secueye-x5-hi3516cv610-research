package anet.channel.thread;

import anet.channel.util.ALog;
import com.huawei.hms.push.constant.RemoteMessageConst;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class ThreadPoolExecutorFactory {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static ScheduledThreadPoolExecutor f1924a = new ScheduledThreadPoolExecutor(1, new b("AWCN Scheduler"));

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static ThreadPoolExecutor f1925b = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, new LinkedBlockingDeque(), new b("AWCN Worker(H)"));

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static ThreadPoolExecutor f1926c = new anet.channel.thread.a(16, 16, 60, TimeUnit.SECONDS, new PriorityBlockingQueue(), new b("AWCN Worker(M)"));

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static ThreadPoolExecutor f1927d = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, new LinkedBlockingDeque(), new b("AWCN Worker(L)"));
    private static ThreadPoolExecutor e = new ThreadPoolExecutor(32, 32, 60, TimeUnit.SECONDS, new LinkedBlockingDeque(), new b("AWCN Worker(Backup)"));
    private static ThreadPoolExecutor f = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new LinkedBlockingDeque(), new b("AWCN Detector"));
    private static ThreadPoolExecutor g = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new LinkedBlockingDeque(), new b("AWCN HR"));
    private static ThreadPoolExecutor h = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new LinkedBlockingDeque(), new b("AWCN Cookie"));

    /* JADX INFO: compiled from: Taobao */
    public static class Priority {
        public static int HIGH = 0;
        public static int LOW = 9;
        public static int NORMAL = 1;
    }

    static {
        f1925b.allowCoreThreadTimeOut(true);
        f1926c.allowCoreThreadTimeOut(true);
        f1927d.allowCoreThreadTimeOut(true);
        e.allowCoreThreadTimeOut(true);
        f.allowCoreThreadTimeOut(true);
        g.allowCoreThreadTimeOut(true);
        h.allowCoreThreadTimeOut(true);
    }

    /* JADX INFO: compiled from: Taobao */
    private static class b implements ThreadFactory {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        AtomicInteger f1931a = new AtomicInteger(0);

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        String f1932b;

        b(String str) {
            this.f1932b = str;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, this.f1932b + this.f1931a.incrementAndGet());
            ALog.i("awcn.ThreadPoolExecutorFactory", "thread created!", null, "name", thread.getName());
            thread.setPriority(5);
            return thread;
        }
    }

    public static Future<?> submitScheduledTask(Runnable runnable) {
        return f1924a.submit(runnable);
    }

    public static Future<?> submitScheduledTask(Runnable runnable, long j, TimeUnit timeUnit) {
        return f1924a.schedule(runnable, j, timeUnit);
    }

    public static void removeScheduleTask(Runnable runnable) {
        f1924a.remove(runnable);
    }

    public static Future<?> submitPriorityTask(Runnable runnable, int i) {
        if (ALog.isPrintLog(1)) {
            ALog.d("awcn.ThreadPoolExecutorFactory", "submit priority task", null, RemoteMessageConst.Notification.PRIORITY, Integer.valueOf(i));
        }
        if (i < Priority.HIGH || i > Priority.LOW) {
            i = Priority.LOW;
        }
        if (i == Priority.HIGH) {
            return f1925b.submit(runnable);
        }
        if (i == Priority.LOW) {
            return f1927d.submit(runnable);
        }
        return f1926c.submit(new a(runnable, i));
    }

    public static Future<?> submitBackupTask(Runnable runnable) {
        return e.submit(runnable);
    }

    public static Future<?> submitDetectTask(Runnable runnable) {
        return f.submit(runnable);
    }

    public static Future<?> submitHRTask(Runnable runnable) {
        return g.submit(runnable);
    }

    public static Future<?> submitCookieMonitor(Runnable runnable) {
        return h.submit(runnable);
    }

    /* JADX INFO: compiled from: Taobao */
    static class a implements Comparable<a>, Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        Runnable f1928a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        int f1929b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        long f1930c;

        public a(Runnable runnable, int i) {
            this.f1928a = null;
            this.f1929b = 0;
            this.f1930c = System.currentTimeMillis();
            this.f1928a = runnable;
            this.f1929b = i;
            this.f1930c = System.currentTimeMillis();
        }

        @Override // java.lang.Comparable
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compareTo(a aVar) {
            int i = this.f1929b;
            int i2 = aVar.f1929b;
            return i != i2 ? i - i2 : (int) (aVar.f1930c - this.f1930c);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f1928a.run();
        }
    }

    public static synchronized void setNormalExecutorPoolSize(int i) {
        if (i < 6) {
            i = 6;
        }
        f1926c.setCorePoolSize(i);
        f1926c.setMaximumPoolSize(i);
    }
}
