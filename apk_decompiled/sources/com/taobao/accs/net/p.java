package com.taobao.accs.net;

import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.AccsState;
import com.taobao.accs.data.Message;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import java.util.Iterator;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class p implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f6398a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ String f6399b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ int f6400c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ boolean f6401d;
    final /* synthetic */ j e;

    p(j jVar, int i, String str, int i2, boolean z) {
        this.e = jVar;
        this.f6398a = i;
        this.f6399b = str;
        this.f6400c = i2;
        this.f6401d = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        Message.a next;
        Message messageB;
        AccsState.getInstance().b(this.e.m, AccsState.RECENT_ERRORS, "oe " + this.f6398a + " " + this.f6399b);
        int i = this.f6400c;
        if (i > 0) {
            Message.a aVar = new Message.a(i, "");
            Iterator<Message.a> it = this.e.e.f().iterator();
            while (true) {
                if (!it.hasNext()) {
                    next = null;
                    break;
                } else {
                    next = it.next();
                    if (next.equals(aVar)) {
                        break;
                    }
                }
            }
            if (next != null && (messageB = this.e.e.b(next.b())) != null) {
                if (this.f6401d) {
                    if (!this.e.a(messageB, 2000)) {
                        this.e.e.a(messageB, AccsErrorCode.convertNetworkSdkError(this.f6398a, this.f6399b).detail(AccsErrorCode.getAllDetails(null)).build());
                    }
                    if (messageB.e() != null) {
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, "total_tnet", 0.0d);
                    }
                } else {
                    this.e.e.a(messageB, AccsErrorCode.convertNetworkSdkError(this.f6398a, this.f6399b).detail(AccsErrorCode.getAllDetails(null)).build());
                }
            }
        }
        int i2 = this.f6400c;
        if (i2 >= 0 || !this.f6401d) {
            return;
        }
        this.e.b(i2);
    }
}
