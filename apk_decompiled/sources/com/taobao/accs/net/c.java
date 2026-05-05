package com.taobao.accs.net;

import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.data.Message;
import com.taobao.accs.utl.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f6367a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ long f6368b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ boolean f6369c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ b f6370d;

    c(b bVar, String str, long j, boolean z) {
        this.f6370d = bVar;
        this.f6367a = str;
        this.f6368b = j;
        this.f6369c = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        Message messageA = this.f6370d.e.a(this.f6367a);
        if (messageA != null) {
            this.f6370d.e.a(messageA, AccsErrorCode.REQ_TIME_OUT.copy().msg("发送超过" + this.f6368b + "未收到回执").detail(AccsErrorCode.getAllDetails(null)).build());
            this.f6370d.a(this.f6367a, this.f6369c, "receive data time out");
            ALog.e(this.f6370d.d(), this.f6367a + "-> receive data time out!", new Object[0]);
        }
    }
}
