package com.alibaba.sdk.android.logger.b;

import com.alibaba.sdk.android.logger.LogLevel;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final LogLevel f2904a = LogLevel.WARN;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f2905b = true;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private LogLevel f2906c = f2904a;

    public void a(LogLevel logLevel) {
        this.f2906c = logLevel;
    }

    public void a(boolean z) {
        this.f2905b = z;
    }

    public boolean b(LogLevel logLevel) {
        return this.f2905b && logLevel.ordinal() >= this.f2906c.ordinal();
    }
}
