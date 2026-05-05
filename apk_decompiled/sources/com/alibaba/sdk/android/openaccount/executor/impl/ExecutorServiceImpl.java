package com.alibaba.sdk.android.openaccount.executor.impl;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import io.netty.util.internal.StringUtil;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public final class ExecutorServiceImpl implements ExecutorService, java.util.concurrent.ExecutorService {
    public static final ExecutorServiceImpl INSTANCE = new ExecutorServiceImpl();
    public static final String TAG = "oa_ExecutorServiceImpl";
    private final Handler handler;
    private ThreadPoolExecutor mExecutor;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final BlockingQueue<Runnable> mPoolWorkQueue = new LinkedBlockingQueue(128);
    private final HandlerThread handlerThread = new HandlerThread("SDK Looper Thread");

    public ExecutorServiceImpl() {
        this.handlerThread.start();
        synchronized (this.handlerThread) {
            while (this.handlerThread.getLooper() == null) {
                try {
                    this.handlerThread.wait();
                } catch (InterruptedException unused) {
                }
            }
        }
        this.handler = new Handler(this.handlerThread.getLooper());
        ThreadFactory threadFactory = new ThreadFactory() { // from class: com.alibaba.sdk.android.openaccount.executor.impl.ExecutorServiceImpl.1
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "ExecutorTask #" + this.mCount.getAndIncrement());
            }
        };
        TimeUnit timeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> blockingQueue = this.mPoolWorkQueue;
        this.mExecutor = new ThreadPoolExecutor(8, 16, 1, timeUnit, blockingQueue, threadFactory, new CoordinatorRejectHandler(blockingQueue));
    }

    @Override // com.alibaba.sdk.android.openaccount.executor.ExecutorService
    public void postHandlerTask(Runnable runnable) {
        this.handler.post(runnable);
    }

    @Override // com.alibaba.sdk.android.openaccount.executor.ExecutorService
    public void postUITask(final Runnable runnable) {
        this.mainHandler.post(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.executor.impl.ExecutorServiceImpl.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    runnable.run();
                } catch (Throwable th) {
                    AliSDKLogger.e(ExecutorServiceImpl.TAG, th.getMessage(), th);
                }
            }
        });
    }

    @Override // com.alibaba.sdk.android.openaccount.executor.ExecutorService
    public void postTask(Runnable runnable) {
        this.mExecutor.execute(runnable);
    }

    public static class CoordinatorRejectHandler implements RejectedExecutionHandler {
        private BlockingQueue<Runnable> mPoolWorkQueue;

        public CoordinatorRejectHandler(BlockingQueue<Runnable> blockingQueue) {
            this.mPoolWorkQueue = blockingQueue;
        }

        @Override // java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            Object[] array = this.mPoolWorkQueue.toArray();
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (Object obj : array) {
                if (obj.getClass().isAnonymousClass()) {
                    sb.append(getOuterClass(obj));
                    sb.append(StringUtil.COMMA);
                    sb.append(' ');
                } else {
                    sb.append(obj.getClass());
                    sb.append(StringUtil.COMMA);
                    sb.append(' ');
                }
            }
            sb.append(']');
            throw new RejectedExecutionException("Task " + runnable.toString() + " rejected from " + threadPoolExecutor.toString() + " in " + sb.toString());
        }

        private Object getOuterClass(Object obj) {
            try {
                Field declaredField = obj.getClass().getDeclaredField("this$0");
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            } catch (Exception e) {
                e.printStackTrace();
                return obj;
            }
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.executor.ExecutorService
    public Looper getDefaultLooper() {
        return this.handlerThread.getLooper();
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        this.mExecutor.execute(runnable);
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        this.mExecutor.shutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        return this.mExecutor.shutdownNow();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return this.mExecutor.isShutdown();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return this.mExecutor.isTerminated();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.mExecutor.awaitTermination(j, timeUnit);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> callable) {
        return this.mExecutor.submit(callable);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Runnable runnable, T t) {
        return this.mExecutor.submit(runnable, t);
    }

    @Override // java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable runnable) {
        return this.mExecutor.submit(runnable);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        return this.mExecutor.invokeAll(collection);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws InterruptedException {
        return this.mExecutor.invokeAll(collection, j, timeUnit);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws ExecutionException, InterruptedException {
        return (T) this.mExecutor.invokeAny(collection);
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return (T) this.mExecutor.invokeAny(collection, j, timeUnit);
    }
}
