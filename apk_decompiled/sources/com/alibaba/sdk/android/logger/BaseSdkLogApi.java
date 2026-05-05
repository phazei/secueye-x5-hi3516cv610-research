package com.alibaba.sdk.android.logger;

import com.alibaba.sdk.android.logger.a.a;
import com.alibaba.sdk.android.logger.b.b;
import com.alibaba.sdk.android.logger.b.c;
import com.alibaba.sdk.android.logger.b.e;
import com.alibaba.sdk.android.logger.b.f;
import com.alibaba.sdk.android.logger.b.g;
import com.alibaba.sdk.android.logger.b.h;
import com.alibaba.sdk.android.logger.interceptor.InterceptorManager;

/* JADX INFO: loaded from: classes.dex */
public class BaseSdkLogApi {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private g f2899a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private a f2901c = new a();
    private b e = new b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private e f2900b = new e(this.e);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private InterceptorManager f2902d = new InterceptorManager(new com.alibaba.sdk.android.logger.interceptor.a(new h(this.f2901c)), new com.alibaba.sdk.android.logger.interceptor.b(this.f2900b));

    public BaseSdkLogApi(String str, boolean z) {
        this.f2899a = new g(str);
        this.f2902d.a(new c(this.f2900b));
        if (z) {
            this.e.a(LogLevel.DEBUG);
            this.f2902d.a(new com.alibaba.sdk.android.logger.b.a());
        }
    }

    public void addILogger(ILogger iLogger) {
        this.f2900b.b(iLogger);
    }

    public <T> void addObjectFormat(Class<T> cls, IObjectLogFormat<T> iObjectLogFormat) {
        this.f2901c.a(cls, iObjectLogFormat);
    }

    public void enable(boolean z) {
        this.e.a(z);
    }

    public LogBuilder getLogBuilder(Object obj) {
        return new LogBuilder(this.f2902d, obj, this.f2899a);
    }

    public ILog getLogger(Object obj) {
        return new f(this.f2899a.a(obj), this.f2902d);
    }

    public void removeILogger(ILogger iLogger) {
        this.f2900b.c(iLogger);
    }

    public void setILogger(ILogger iLogger) {
        this.f2900b.a(iLogger);
    }

    public void setLevel(LogLevel logLevel) {
        this.e.a(logLevel);
    }
}
