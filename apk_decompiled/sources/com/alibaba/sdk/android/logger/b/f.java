package com.alibaba.sdk.android.logger.b;

import com.alibaba.sdk.android.logger.ILog;
import com.alibaba.sdk.android.logger.LogLevel;
import com.alibaba.sdk.android.logger.interceptor.InterceptorManager;

/* JADX INFO: loaded from: classes.dex */
public class f implements ILog {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f2913a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private InterceptorManager f2914b;

    public f(String str, InterceptorManager interceptorManager) {
        this.f2913a = str;
        this.f2914b = interceptorManager;
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void d(String str) {
        d(str);
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void d(Object... objArr) {
        this.f2914b.a(LogLevel.DEBUG, this.f2913a, objArr);
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void e(String str) {
        e(str);
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void e(String str, Throwable th) {
        e(str, th);
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void e(Object... objArr) {
        this.f2914b.a(LogLevel.ERROR, this.f2913a, objArr);
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void i(String str) {
        i(str);
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void i(Object... objArr) {
        this.f2914b.a(LogLevel.INFO, this.f2913a, objArr);
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void w(String str) {
        w(str);
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void w(String str, Throwable th) {
        w(str, th);
    }

    @Override // com.alibaba.sdk.android.logger.ILog
    public void w(Object... objArr) {
        this.f2914b.a(LogLevel.WARN, this.f2913a, objArr);
    }
}
