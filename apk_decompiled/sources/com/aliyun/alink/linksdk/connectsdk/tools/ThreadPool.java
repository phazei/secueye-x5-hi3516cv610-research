package com.aliyun.alink.linksdk.connectsdk.tools;

import android.os.Looper;
import android.util.Log;
import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public class ThreadPool {
    private static final String TAG = "ThreadPool";
    private int KEEP_ALIVE_TIME;
    private BlockingQueue<Runnable> mBlockQueue;
    private int mCoreSize;
    private int mQueueSize;
    private ThreadPoolExecutor mThreadPool;

    public static class DefaultThreadPool {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        ThreadPool f4214a;

        static class SingletonHolder {
            private static final DefaultThreadPool INSTANCE = new DefaultThreadPool();

            private SingletonHolder() {
            }
        }

        private DefaultThreadPool() {
            this.f4214a = new ThreadPool();
        }

        public static final DefaultThreadPool getInstance() {
            return SingletonHolder.INSTANCE;
        }

        public Future<?> submit(Runnable runnable) {
            return this.f4214a.submit(runnable);
        }
    }

    public static class MainThreadHandler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        WeakHandler f4215a;

        static class SingletonHolder {
            private static final MainThreadHandler INSTANCE = new MainThreadHandler();

            private SingletonHolder() {
            }
        }

        private MainThreadHandler() {
            this.f4215a = new WeakHandler(Looper.getMainLooper());
        }

        public static final MainThreadHandler getInstance() {
            return SingletonHolder.INSTANCE;
        }

        public void post(Runnable runnable) {
            this.f4215a.post(runnable);
        }

        public void post(Runnable runnable, long j) {
            this.f4215a.postDelayed(runnable, j);
        }
    }

    public ThreadPool() {
        this.KEEP_ALIVE_TIME = 8;
        this.mCoreSize = getCoresNumbers();
        this.mQueueSize = this.mCoreSize * 32;
        a();
    }

    public ThreadPool(int i, int i2) {
        this.KEEP_ALIVE_TIME = 8;
        this.mCoreSize = i;
        this.mQueueSize = i2;
        a();
    }

    void a() {
        this.mCoreSize = Math.min(4, this.mCoreSize);
        this.mBlockQueue = new ArrayBlockingQueue(this.mQueueSize);
        int i = this.mCoreSize;
        this.mThreadPool = new ThreadPoolExecutor(i, i * 2, this.KEEP_ALIVE_TIME, TimeUnit.SECONDS, this.mBlockQueue, new ThreadFactory() { // from class: com.aliyun.alink.linksdk.connectsdk.tools.ThreadPool.1

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            AtomicInteger f4211a = new AtomicInteger(1);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "ALinke-Thread-Pool-" + this.f4211a.getAndDecrement());
            }
        }, new RejectedExecutionHandler() { // from class: com.aliyun.alink.linksdk.connectsdk.tools.ThreadPool.2
            @Override // java.util.concurrent.RejectedExecutionHandler
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                Log.e(ThreadPool.TAG, "rejectedExecution");
                Log.e(ThreadPool.TAG, ThreadPool.this.mBlockQueue.size() + "");
            }
        });
    }

    public Future<?> submit(Runnable runnable) {
        return this.mThreadPool.submit(runnable);
    }

    private static int getCoresNumbers() {
        int iAvailableProcessors;
        try {
            iAvailableProcessors = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() { // from class: com.aliyun.alink.linksdk.connectsdk.tools.ThreadPool.1CpuFilter
                @Override // java.io.FileFilter
                public boolean accept(File file) {
                    return Pattern.matches("cpu[0-9]+", file.getName());
                }
            }).length;
        } catch (Exception e) {
            e.printStackTrace();
            iAvailableProcessors = 0;
        }
        if (iAvailableProcessors < 1) {
            iAvailableProcessors = Runtime.getRuntime().availableProcessors();
        }
        if (iAvailableProcessors < 1) {
            iAvailableProcessors = 1;
        }
        Log.i(TAG, "CPU cores: " + iAvailableProcessors);
        return iAvailableProcessors;
    }
}
