package com.alibaba.sdk.android.logger;

import com.alibaba.sdk.android.logger.b.f;
import com.alibaba.sdk.android.logger.b.g;
import com.alibaba.sdk.android.logger.interceptor.ILogInterceptor;
import com.alibaba.sdk.android.logger.interceptor.InterceptorManager;
import com.alibaba.sdk.android.logger.interceptor.c;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class LogBuilder {
    private Object instanceObject;
    private ArrayList<ILogInterceptor> logInterceptors = new ArrayList<>();
    private ArrayList<c> loggerInterceptors = new ArrayList<>();
    private InterceptorManager originInterceptorManager;
    private g tagGenerator;

    public LogBuilder(InterceptorManager interceptorManager, Object obj, g gVar) {
        this.originInterceptorManager = interceptorManager;
        this.instanceObject = obj;
        this.tagGenerator = gVar;
    }

    public LogBuilder addLogInterceptor(ILogInterceptor iLogInterceptor) {
        this.logInterceptors.add(iLogInterceptor);
        return this;
    }

    public LogBuilder addLoggerInterceptor(c cVar) {
        this.loggerInterceptors.add(cVar);
        return this;
    }

    public ILog build() {
        InterceptorManager interceptorManagerA = this.originInterceptorManager.a();
        Iterator<ILogInterceptor> it = this.logInterceptors.iterator();
        while (it.hasNext()) {
            interceptorManagerA.a(it.next());
        }
        Iterator<c> it2 = this.loggerInterceptors.iterator();
        while (it2.hasNext()) {
            interceptorManagerA.a(it2.next());
        }
        return new f(this.tagGenerator.a(this.instanceObject), interceptorManagerA);
    }
}
