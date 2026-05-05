package com.aliyun.alink.h2.utils;

import com.aliyun.alink.h2.b.b;

/* JADX INFO: loaded from: classes2.dex */
public class HLoggerFactory {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ILogger f3941a = null;

    public void setLogger(ILogger iLogger) {
        this.f3941a = iLogger;
    }

    public ILogger getInstance(String str) {
        ILogger iLogger = this.f3941a;
        return iLogger != null ? iLogger : new b(str);
    }
}
