package com.taobao.accs.utl;

import anet.channel.util.ALog;
import com.alibaba.sdk.android.logger.ILog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class j implements ALog.ILog {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ILog f6464a = AccsLogger.getLogger("NetworkSdk");

    @Override // anet.channel.util.ALog.ILog
    public boolean isPrintLog(int i) {
        return true;
    }

    @Override // anet.channel.util.ALog.ILog
    public boolean isValid() {
        return true;
    }

    @Override // anet.channel.util.ALog.ILog
    public void setLogLevel(int i) {
    }

    @Override // anet.channel.util.ALog.ILog
    public void d(String str, String str2) {
        this.f6464a.d(a(str, str2));
    }

    @Override // anet.channel.util.ALog.ILog
    public void i(String str, String str2) {
        this.f6464a.i(a(str, str2));
    }

    @Override // anet.channel.util.ALog.ILog
    public void w(String str, String str2) {
        this.f6464a.w(a(str, str2));
    }

    @Override // anet.channel.util.ALog.ILog
    public void w(String str, String str2, Throwable th) {
        this.f6464a.w(a(str, str2), th);
    }

    @Override // anet.channel.util.ALog.ILog
    public void e(String str, String str2) {
        this.f6464a.e(a(str, str2));
    }

    @Override // anet.channel.util.ALog.ILog
    public void e(String str, String str2, Throwable th) {
        this.f6464a.e(a(str, str2), th);
    }

    private String a(String str, String str2) {
        return "[" + str + "]" + str2;
    }
}
