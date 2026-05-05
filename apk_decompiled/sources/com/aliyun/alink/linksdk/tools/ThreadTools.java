package com.aliyun.alink.linksdk.tools;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes2.dex */
public class ThreadTools {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static LoopHandler f4443a = new LoopHandler(Looper.getMainLooper());

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static ExecutorService f4444b = null;

    public static void runOnUiThread(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        f4443a.enqueue(runnable);
    }

    public static String getProcessName(Context context, int i) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
        if (activityManager != null && (runningAppProcesses = activityManager.getRunningAppProcesses()) != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == i) {
                    return runningAppProcessInfo.processName;
                }
            }
        }
        return null;
    }

    public static boolean isAppBroughtToBackgroundByTask(Application application) {
        try {
            List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) application.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME)).getRunningTasks(1);
            if (runningTasks.isEmpty()) {
                return false;
            }
            ComponentName componentName = runningTasks.get(0).topActivity;
            return !componentName.getPackageName().equals(application.getPackageName());
        } catch (Exception unused) {
            return true;
        }
    }

    public static void submitTask(Runnable runnable, boolean z) {
        submitTask(runnable, z, 0);
    }

    public static void submitTask(Runnable runnable, boolean z, int i) {
        if (z) {
            b().enqueue(runnable, i);
        } else {
            a().submit(runnable);
        }
    }

    public static final class LoopHandler extends Handler {
        public LoopHandler(Looper looper) {
            super(looper);
        }

        public void enqueue(Runnable runnable) {
            enqueue(runnable, 0);
        }

        public void enqueue(Runnable runnable, int i) {
            Message messageObtainMessage = obtainMessage();
            messageObtainMessage.obj = runnable;
            sendMessageDelayed(messageObtainMessage, i);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message == null || message.obj == null || !(message.obj instanceof Runnable)) {
                return;
            }
            try {
                ((Runnable) message.obj).run();
            } catch (Exception e) {
                ALog.e("ThreadTools_ThreadTools", "run task error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static ExecutorService a() {
        if (f4444b == null) {
            c();
        }
        return f4444b;
    }

    private static LoopHandler b() {
        if (f4443a == null) {
            f4443a = new LoopHandler(Looper.myLooper());
        }
        return f4443a;
    }

    private static void c() {
        int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
        if (iAvailableProcessors > 10) {
            f4444b = new ThreadPoolExecutor(iAvailableProcessors, iAvailableProcessors + 7, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(15), new ALXHDefaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        } else {
            f4444b = new ThreadPoolExecutor(iAvailableProcessors, 15, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(15), new ALXHDefaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    static class ALXHDefaultThreadFactory implements ThreadFactory {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final ThreadGroup f4446b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final String f4448d;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final AtomicInteger f4445a = new AtomicInteger(1);

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final AtomicInteger f4447c = new AtomicInteger(1);

        ALXHDefaultThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.f4446b = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.f4448d = "pool-" + this.f4445a.getAndIncrement() + "-thread-";
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.f4446b, runnable, this.f4448d + this.f4447c.getAndIncrement(), 0L);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            Process.setThreadPriority(10);
            return thread;
        }
    }
}
