package com.alibaba.sdk.android.openaccount.trace;

import android.content.Context;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import java.lang.Thread;

/* JADX INFO: loaded from: classes.dex */
public class UncaughtExceptionTraceHandler implements Thread.UncaughtExceptionHandler {
    private boolean inited;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public synchronized void init(Context context) {
        if (this.inited) {
            return;
        }
        this.inited = true;
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (this.mDefaultHandler == null || !(this.mDefaultHandler.equals(this) || this.mDefaultHandler.getClass().equals(UncaughtExceptionTraceHandler.class))) {
            this.mDefaultHandler = defaultUncaughtExceptionHandler;
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        traceException(thread, th);
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.mDefaultHandler;
        if (uncaughtExceptionHandler == null || uncaughtExceptionHandler == this || uncaughtExceptionHandler.getClass().equals(UncaughtExceptionTraceHandler.class)) {
            return;
        }
        this.mDefaultHandler.uncaughtException(thread, th);
    }

    private void traceException(Thread thread, Throwable th) {
        if (th == null) {
            return;
        }
        AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "UncaughtException" + thread.getName(), th);
    }
}
