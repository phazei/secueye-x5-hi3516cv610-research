package com.aliyun.alink.linksdk.tools.log;

import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class LogerImpl implements ILogger {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f4451a;

    public LogerImpl(String str) {
        this.f4451a = null;
        this.f4451a = str;
    }

    @Override // com.aliyun.alink.linksdk.tools.log.ILogger
    public void d(String str, String str2) {
        ALog.d(this.f4451a + str, str2);
    }

    @Override // com.aliyun.alink.linksdk.tools.log.ILogger
    public void i(String str, String str2) {
        ALog.i(this.f4451a + str, str2);
    }

    @Override // com.aliyun.alink.linksdk.tools.log.ILogger
    public void w(String str, String str2) {
        ALog.w(this.f4451a + str, str2);
    }

    @Override // com.aliyun.alink.linksdk.tools.log.ILogger
    public void e(String str, String str2) {
        ALog.e(this.f4451a + str, str2);
    }

    @Override // com.aliyun.alink.linksdk.tools.log.ILogger
    public void e(String str, String str2, Exception exc) {
        ALog.e(this.f4451a + str, str2, exc);
    }
}
