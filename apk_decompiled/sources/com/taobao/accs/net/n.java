package com.taobao.accs.net;

import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.data.Message;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class n implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f6390a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ long f6391b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ boolean f6392c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ j f6393d;

    n(j jVar, String str, long j, boolean z) {
        this.f6393d = jVar;
        this.f6390a = str;
        this.f6391b = j;
        this.f6392c = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        Message messageA = this.f6393d.e.a(this.f6390a);
        if (messageA != null) {
            this.f6393d.e.a(messageA, AccsErrorCode.REQ_TIME_OUT.copy().msg("发送超过" + this.f6391b + "未收到回执").detail(AccsErrorCode.getAllDetails(null)).build());
            this.f6393d.a(this.f6390a, this.f6392c, "receive data time out");
            this.f6393d.t.e(this.f6390a + "-> receive data time out!");
        }
    }
}
