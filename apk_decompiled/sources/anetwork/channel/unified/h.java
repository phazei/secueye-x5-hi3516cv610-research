package anetwork.channel.unified;

import anet.channel.Session;
import anet.channel.SessionCenter;
import anet.channel.SessionGetCallback;
import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.HttpUrl;
import io.netty.handler.codec.rtsp.RtspHeaders;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class h implements SessionGetCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ RequestStatistic f2071a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ long f2072b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ Request f2073c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ SessionCenter f2074d;
    final /* synthetic */ HttpUrl e;
    final /* synthetic */ boolean f;
    final /* synthetic */ e g;

    h(e eVar, RequestStatistic requestStatistic, long j, Request request, SessionCenter sessionCenter, HttpUrl httpUrl, boolean z) {
        this.g = eVar;
        this.f2071a = requestStatistic;
        this.f2072b = j;
        this.f2073c = request;
        this.f2074d = sessionCenter;
        this.e = httpUrl;
        this.f = z;
    }

    @Override // anet.channel.SessionGetCallback
    public void onSessionGetSuccess(Session session) {
        ALog.i(e.TAG, "onSessionGetSuccess", this.g.f2059a.f2080c, RtspHeaders.Names.SESSION, session);
        this.f2071a.connWaitTime = System.currentTimeMillis() - this.f2072b;
        this.f2071a.spdyRequestSend = true;
        this.g.a(session, this.f2073c);
    }

    @Override // anet.channel.SessionGetCallback
    public void onSessionGetFail() {
        ALog.e(e.TAG, "onSessionGetFail", this.g.f2059a.f2080c, "url", this.f2071a.url);
        this.f2071a.connWaitTime = System.currentTimeMillis() - this.f2072b;
        e eVar = this.g;
        eVar.a(eVar.a(null, this.f2074d, this.e, this.f), this.f2073c);
    }
}
