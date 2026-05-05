package com.alibaba.ailabs.tg.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class ThreadPoolExecutorFactory {
    private static int CORE_POOL_SIZE = 3;
    private static int KEEP_ALIVE_TIME = 60;
    private static int MAX_POOL_SIZE = 5;
    private static final int QUEENCOUNT = 30;
    private static final AtomicInteger integer = new AtomicInteger();
    private static int prop = 5;
    private static ThreadPoolExecutor threadPoolExecutor;

    static class TBThreadFactory implements ThreadFactory {
        private int priority;

        public TBThreadFactory(int i) {
            this.priority = i;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "TgTask:" + ThreadPoolExecutorFactory.integer.getAndIncrement());
            thread.setPriority(this.priority);
            return thread;
        }
    }

    public static ThreadPoolExecutor createExecutor(int i, int i2, int i3, int i4, int i5) {
        LinkedBlockingQueue linkedBlockingQueue;
        if (i5 > 0) {
            linkedBlockingQueue = new LinkedBlockingQueue(i5);
        } else {
            linkedBlockingQueue = new LinkedBlockingQueue();
        }
        return new ThreadPoolExecutor(i2, i3, i4, TimeUnit.SECONDS, linkedBlockingQueue, new TBThreadFactory(i), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public static synchronized ThreadPoolExecutor getDefaulThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
            int i = (iAvailableProcessors * 2) - 1;
            if (iAvailableProcessors < 4) {
                iAvailableProcessors = CORE_POOL_SIZE;
                i = MAX_POOL_SIZE;
            }
            threadPoolExecutor = createExecutor(prop, iAvailableProcessors, i, KEEP_ALIVE_TIME, 30);
        }
        return threadPoolExecutor;
    }
}
