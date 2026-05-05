package com.alibaba.sdk.android.logger.interceptor;

import com.alibaba.sdk.android.logger.ILogger;
import com.alibaba.sdk.android.logger.LogLevel;

/* JADX INFO: loaded from: classes.dex */
public class a implements ILogInterceptor {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private d f2923a;

    public a(d dVar) {
        this.f2923a = dVar;
    }

    @Override // com.alibaba.sdk.android.logger.interceptor.ILogInterceptor
    public void handle(final InterceptorManager interceptorManager, int i, LogLevel logLevel, String str, Object[] objArr) {
        this.f2923a.a(logLevel, str, objArr, new ILogger() { // from class: com.alibaba.sdk.android.logger.interceptor.a.1
            @Override // com.alibaba.sdk.android.logger.ILogger
            public void print(LogLevel logLevel2, String str2, String str3) {
                interceptorManager.toNextLoggerInterceptor(-1, logLevel2, str2, str3);
            }
        });
    }
}
