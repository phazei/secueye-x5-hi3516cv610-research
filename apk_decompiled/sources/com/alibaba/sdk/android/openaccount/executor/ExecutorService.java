package com.alibaba.sdk.android.openaccount.executor;

import android.os.Looper;

/* JADX INFO: loaded from: classes.dex */
public interface ExecutorService {
    Looper getDefaultLooper();

    void postHandlerTask(Runnable runnable);

    void postTask(Runnable runnable);

    void postUITask(Runnable runnable);
}
