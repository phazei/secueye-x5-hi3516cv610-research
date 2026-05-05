package com.alibaba.sdk.android.push.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class ThreadUtil {
    private static final ExecutorService POOL = new ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());

    public static ExecutorService getExecutor() {
        return POOL;
    }
}
