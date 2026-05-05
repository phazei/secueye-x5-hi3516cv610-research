package com.alibaba.sdk.android.openaccount.task;

import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbsRunnable implements Runnable {
    private static final String TAG = "AbsRunnable";

    public abstract void runWithoutException();

    @Override // java.lang.Runnable
    public void run() {
        try {
            runWithoutException();
        } catch (Throwable th) {
            AliSDKLogger.e(TAG, th.getMessage(), th);
        }
    }
}
