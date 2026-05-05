package com.alibaba.sdk.android.logger.b;

import com.alibaba.sdk.android.logger.LogLevel;
import com.alibaba.sdk.android.logger.interceptor.ILogInterceptor;
import com.alibaba.sdk.android.logger.interceptor.InterceptorManager;

/* JADX INFO: loaded from: classes.dex */
public class c implements ILogInterceptor {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private e f2907a;

    public c(e eVar) {
        this.f2907a = eVar;
    }

    @Override // com.alibaba.sdk.android.logger.interceptor.ILogInterceptor
    public void handle(InterceptorManager interceptorManager, int i, LogLevel logLevel, String str, Object[] objArr) {
        if (this.f2907a.a(logLevel)) {
            interceptorManager.toNextLogInterceptor(i, logLevel, str, objArr);
        }
    }
}
