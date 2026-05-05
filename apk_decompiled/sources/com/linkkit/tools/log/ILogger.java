package com.linkkit.tools.log;

/* JADX INFO: loaded from: classes3.dex */
public interface ILogger {
    void d(String str, String str2);

    void e(String str, String str2);

    void e(String str, String str2, Throwable th);

    void i(String str, String str2);

    void llog(int i, String str, String str2, Throwable th);

    void setLogLevel(int i);

    void w(String str, String str2);

    void w(String str, String str2, Throwable th);
}
