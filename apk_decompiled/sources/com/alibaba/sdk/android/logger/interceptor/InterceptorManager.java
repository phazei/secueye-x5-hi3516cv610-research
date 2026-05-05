package com.alibaba.sdk.android.logger.interceptor;

import com.alibaba.sdk.android.logger.LogLevel;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class InterceptorManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ArrayList<ILogInterceptor> f2919a = new ArrayList<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ArrayList<c> f2920b = new ArrayList<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private a f2921c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private b f2922d;

    public InterceptorManager(a aVar, b bVar) {
        this.f2921c = aVar;
        this.f2922d = bVar;
    }

    public InterceptorManager a() {
        InterceptorManager interceptorManager = new InterceptorManager(this.f2921c, this.f2922d);
        interceptorManager.f2919a.addAll(this.f2919a);
        interceptorManager.f2920b.addAll(this.f2920b);
        return interceptorManager;
    }

    public void a(LogLevel logLevel, String str, Object[] objArr) {
        toNextLogInterceptor(-1, logLevel, str, objArr);
    }

    public void a(ILogInterceptor iLogInterceptor) {
        this.f2919a.add(iLogInterceptor);
    }

    public void a(c cVar) {
        this.f2920b.add(cVar);
    }

    public void toNextLogInterceptor(int i, LogLevel logLevel, String str, Object[] objArr) {
        int i2 = i + 1;
        if (i2 >= this.f2919a.size()) {
            this.f2921c.handle(this, i2, logLevel, str, objArr);
        } else {
            this.f2919a.get(i2).handle(this, i2, logLevel, str, objArr);
        }
    }

    public void toNextLoggerInterceptor(int i, LogLevel logLevel, String str, String str2) {
        int i2 = i + 1;
        if (i2 >= this.f2920b.size()) {
            this.f2922d.a(this, i2, logLevel, str, str2);
        } else {
            this.f2920b.get(i2).a(this, i2, logLevel, str, str2);
        }
    }
}
