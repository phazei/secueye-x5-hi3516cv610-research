package com.alibaba.sdk.android.push.utils;

import com.alibaba.sdk.android.logger.BaseSdkLogApi;
import com.alibaba.sdk.android.logger.ILog;
import com.alibaba.sdk.android.logger.ILogger;
import com.alibaba.sdk.android.logger.LogLevel;

/* JADX INFO: loaded from: classes.dex */
public class ThirdPushLogger {

    private static class Holder {
        private static final BaseSdkLogApi instance = new BaseSdkLogApi("ThirdPush", false);

        private Holder() {
        }
    }

    public static void enable(boolean z) {
        Holder.instance.enable(z);
    }

    public static void setLevel(LogLevel logLevel) {
        Holder.instance.setLevel(logLevel);
    }

    public static void setILogger(ILogger iLogger) {
        Holder.instance.setILogger(iLogger);
    }

    public static void addILogger(ILogger iLogger) {
        Holder.instance.addILogger(iLogger);
    }

    public static void removeILogger(ILogger iLogger) {
        Holder.instance.removeILogger(iLogger);
    }

    public static ILog getLogger(Object obj) {
        return Holder.instance.getLogger(obj);
    }
}
