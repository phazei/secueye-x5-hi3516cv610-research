package com.alibaba.sdk.android.ams.common.logger;

/* JADX INFO: loaded from: classes.dex */
public interface LoggerListener {
    void d(String str, String str2, Throwable th, int i);

    void e(String str, String str2, Throwable th, int i);

    void i(String str, String str2, Throwable th, int i);

    void w(String str, String str2, Throwable th, int i);
}
