package com.taobao.accs.net;

import anet.channel.session.TnetSpdySession;
import com.taobao.accs.AccsState;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class o implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f6394a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ int f6395b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ byte[] f6396c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ TnetSpdySession f6397d;
    final /* synthetic */ j e;

    o(j jVar, int i, int i2, byte[] bArr, TnetSpdySession tnetSpdySession) {
        this.e = jVar;
        this.f6394a = i;
        this.f6395b = i2;
        this.f6396c = bArr;
        this.f6397d = tnetSpdySession;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.e.t.i("onDataReceive", "type", Integer.valueOf(this.f6394a), Constants.KEY_DATA_ID, Integer.valueOf(this.f6395b));
        AccsState.getInstance().a(this.e.m, AccsState.LAST_MSG_RECEIVE_TIME, Integer.valueOf(this.f6395b));
        if (this.f6394a != 200) {
            this.e.t.e("drop frame len:" + this.f6396c.length + " frameType" + this.f6394a);
            return;
        }
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.e.e.a(this.f6396c, this.f6397d.getHost());
            com.taobao.accs.ut.a.d dVarG = this.e.e.g();
            if (dVarG != null) {
                dVarG.f6432c = String.valueOf(jCurrentTimeMillis);
                dVarG.g = this.e.f6365c == 0 ? "service" : "inapp";
                dVarG.a();
            }
        } catch (Throwable th) {
            this.e.t.e("onDataReceive", th);
            UTMini.getInstance().commitEvent(66001, "DATA_RECEIVE", UtilityImpl.a(th));
        }
    }
}
