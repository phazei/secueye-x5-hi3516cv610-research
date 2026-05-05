package com.alibaba.sdk.android.logger;

/* JADX INFO: loaded from: classes.dex */
public interface ILoggerWithControl extends ILogger {
    boolean isEnabled();

    boolean isPrint(LogLevel logLevel);
}
