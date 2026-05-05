package com.aliyun.alink.linksdk.tools.log;

/* JADX INFO: loaded from: classes2.dex */
public class HLoggerFactory {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ILogger f4449a = null;

    public void setLogger(ILogger iLogger) {
        this.f4449a = iLogger;
    }

    public ILogger getInstance(String str) {
        ILogger iLogger = this.f4449a;
        return iLogger != null ? iLogger : new LogerImpl(str);
    }
}
