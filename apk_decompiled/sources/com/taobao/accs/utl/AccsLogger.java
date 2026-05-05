package com.taobao.accs.utl;

import com.alibaba.sdk.android.logger.BaseSdkLogApi;
import com.alibaba.sdk.android.logger.ILog;
import com.alibaba.sdk.android.logger.ILogger;
import com.alibaba.sdk.android.logger.LogLevel;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class AccsLogger {

    /* JADX INFO: compiled from: Taobao */
    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final BaseSdkLogApi f6449a = new BaseSdkLogApi("EMASNAccs", false);

        private a() {
        }
    }

    public static void enable(boolean z) {
        a.f6449a.enable(z);
    }

    public static void setLevel(LogLevel logLevel) {
        a.f6449a.setLevel(logLevel);
    }

    public static void setILogger(ILogger iLogger) {
        a.f6449a.setILogger(iLogger);
    }

    public static void addILogger(ILogger iLogger) {
        a.f6449a.addILogger(iLogger);
    }

    public static void removeILogger(ILogger iLogger) {
        a.f6449a.removeILogger(iLogger);
    }

    public static ILog getLogger(Object obj) {
        return a.f6449a.getLogger(obj);
    }
}
