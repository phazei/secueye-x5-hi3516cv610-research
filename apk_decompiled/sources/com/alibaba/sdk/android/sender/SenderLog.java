package com.alibaba.sdk.android.sender;

import com.alibaba.sdk.android.logger.BaseSdkLogApi;
import com.alibaba.sdk.android.logger.ILog;
import com.alibaba.sdk.android.logger.ILogger;
import com.alibaba.sdk.android.logger.LogLevel;

/* JADX INFO: loaded from: classes.dex */
public class SenderLog {

    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final BaseSdkLogApi f3183a = new BaseSdkLogApi("Sender", false);
    }

    private SenderLog() {
    }

    public static void addILogger(ILogger iLogger) {
        a.f3183a.addILogger(iLogger);
    }

    public static void enable(boolean z) {
        a.f3183a.enable(z);
    }

    public static ILog getLogger(Object obj) {
        return a.f3183a.getLogger(obj);
    }

    public static void removeILogger(ILogger iLogger) {
        a.f3183a.removeILogger(iLogger);
    }

    public static void setILogger(ILogger iLogger) {
        a.f3183a.setILogger(iLogger);
    }

    public static void setLevel(LogLevel logLevel) {
        a.f3183a.setLevel(logLevel);
    }
}
