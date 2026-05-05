package com.alibaba.sdk.android.logger.b;

import com.alibaba.sdk.android.logger.LogLevel;
import com.alibaba.sdk.android.logger.interceptor.InterceptorManager;

/* JADX INFO: loaded from: classes.dex */
public class a implements com.alibaba.sdk.android.logger.interceptor.c {
    private String a() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            if (!stackTrace[i].getClassName().startsWith("com.alibaba.sdk.android.logger")) {
                return "(" + stackTrace[i].getFileName() + ":" + stackTrace[i].getLineNumber() + ")";
            }
        }
        return "";
    }

    @Override // com.alibaba.sdk.android.logger.interceptor.c
    public void a(InterceptorManager interceptorManager, int i, LogLevel logLevel, String str, String str2) {
        interceptorManager.toNextLoggerInterceptor(i, logLevel, str, str2 + a());
    }
}
