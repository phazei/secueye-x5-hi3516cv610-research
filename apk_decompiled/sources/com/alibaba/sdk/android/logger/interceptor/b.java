package com.alibaba.sdk.android.logger.interceptor;

import com.alibaba.sdk.android.logger.ILogger;
import com.alibaba.sdk.android.logger.LogLevel;

/* JADX INFO: loaded from: classes.dex */
public class b implements c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ILogger f2926a;

    public b(ILogger iLogger) {
        this.f2926a = iLogger;
    }

    @Override // com.alibaba.sdk.android.logger.interceptor.c
    public void a(InterceptorManager interceptorManager, int i, LogLevel logLevel, String str, String str2) {
        this.f2926a.print(logLevel, str, str2);
    }
}
