package com.taobao.accs.net;

import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.utl.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ b f6371a;

    d(b bVar) {
        this.f6371a = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f6371a.e.c()) {
            ALog.e(this.f6371a.d(), "receive ping time out! ", new Object[0]);
            f.a(this.f6371a.f6366d).c();
            this.f6371a.a("", false, "receive ping timeout");
            this.f6371a.e.a(AccsErrorCode.SPDY_PING_TIME_OUT.copy().detail(AccsErrorCode.getAllDetails(null)).build());
        }
    }
}
