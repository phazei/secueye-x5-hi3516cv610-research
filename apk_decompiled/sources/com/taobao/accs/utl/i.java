package com.taobao.accs.utl;

import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class i implements ALog.ILog {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ALog.ILog f6462a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private a f6463b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: compiled from: Taobao */
    public interface a {
        void a(String str);
    }

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

    public i(ALog.ILog iLog, a aVar) {
        this.f6462a = iLog;
        this.f6463b = aVar;
    }

    @Override // anet.channel.util.ALog.ILog
    public void d(String str, String str2) {
        this.f6462a.d(str, str2);
        this.f6463b.a(str2);
    }

    @Override // anet.channel.util.ALog.ILog
    public void i(String str, String str2) {
        this.f6462a.i(str, str2);
        this.f6463b.a(str2);
    }

    @Override // anet.channel.util.ALog.ILog
    public void w(String str, String str2) {
        this.f6462a.w(str, str2);
        this.f6463b.a(str2);
    }

    @Override // anet.channel.util.ALog.ILog
    public void w(String str, String str2, Throwable th) {
        this.f6462a.w(str, str2, th);
        this.f6463b.a(str2 + " " + th.getMessage());
    }

    @Override // anet.channel.util.ALog.ILog
    public void e(String str, String str2) {
        this.f6462a.e(str, str2);
        this.f6463b.a(str2);
    }

    @Override // anet.channel.util.ALog.ILog
    public void e(String str, String str2, Throwable th) {
        this.f6462a.e(str, str2, th);
        this.f6463b.a(str2 + " " + th.getMessage());
    }
}
