package com.aliyun.iot.aep.sdk.threadpool;

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
        ThreadPool mThreadPool;

        static class a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private static final DefaultThreadPool f4895a = new DefaultThreadPool();
        }

        private DefaultThreadPool() {
            this.mThreadPool = new ThreadPool();
        }

        public static final DefaultThreadPool getInstance() {
            return a.f4895a;
        }

        public Future<?> submit(Runnable runnable) {
            return this.mThreadPool.submit(runnable);
        }
    }

    public static class MainThreadHandler {
        WeakHandler mHandler;

        static class a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private static final MainThreadHandler f4896a = new MainThreadHandler();
        }

        private MainThreadHandler() {
            this.mHandler = new WeakHandler(Looper.getMainLooper());
        }

        public static final MainThreadHandler getInstance() {
            return a.f4896a;
        }

        public void post(Runnable runnable) {
            this.mHandler.post(runnable);
        }

        public void post(Runnable runnable, long j) {
            this.mHandler.postDelayed(runnable, j);
        }
    }

    public ThreadPool() {
        this.KEEP_ALIVE_TIME = 8;
        this.mCoreSize = getCoresNumbers();
        this.mQueueSize = this.mCoreSize * 32;
        init();
    }

    public ThreadPool(int i, int i2) {
        this.KEEP_ALIVE_TIME = 8;
        this.mCoreSize = i;
        this.mQueueSize = i2;
        init();
    }

    void init() {
        this.mCoreSize = Math.min(4, this.mCoreSize);
        this.mBlockQueue = new ArrayBlockingQueue(this.mQueueSize);
        int i = this.mCoreSize;
        this.mThreadPool = new ThreadPoolExecutor(i, i * 2, this.KEEP_ALIVE_TIME, TimeUnit.SECONDS, this.mBlockQueue, new ThreadFactory() { // from class: com.aliyun.iot.aep.sdk.threadpool.ThreadPool.1

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            AtomicInteger f4892a = new AtomicInteger(1);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "ALinke-Thread-Pool-" + this.f4892a.getAndDecrement());
            }
        }, new RejectedExecutionHandler() { // from class: com.aliyun.iot.aep.sdk.threadpool.ThreadPool.2
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

    class a implements FileFilter {
        a() {
        }

        @Override // java.io.FileFilter
        public boolean accept(File file) {
            return Pattern.matches("cpu[0-9]+", file.getName());
        }
    }

    private static int getCoresNumbers() {
        int iAvailableProcessors;
        try {
            iAvailableProcessors = new File("/sys/devices/system/cpu/").listFiles(new a()).length;
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
