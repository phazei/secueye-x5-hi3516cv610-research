package com.alibaba.sdk.android.logger;

/* JADX INFO: loaded from: classes.dex */
public interface ILog {
    void d(String str);

    void d(Object... objArr);

    void e(String str);

    void e(String str, Throwable th);

    void e(Object... objArr);

    void i(String str);

    void i(Object... objArr);

    void w(String str);

    void w(String str, Throwable th);

    void w(Object... objArr);
}
