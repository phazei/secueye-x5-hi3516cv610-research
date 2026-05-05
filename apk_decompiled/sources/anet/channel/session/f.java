package anet.channel.session;

import anet.channel.RequestCb;
import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Request f1824a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ RequestCb f1825b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ RequestStatistic f1826c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ d f1827d;

    f(d dVar, Request request, RequestCb requestCb, RequestStatistic requestStatistic) {
        this.f1827d = dVar;
        this.f1824a = request;
        this.f1825b = requestCb;
        this.f1826c = requestStatistic;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f1824a.f1794a.sendBeforeTime = System.currentTimeMillis() - this.f1824a.f1794a.reqStart;
        b.a(this.f1824a, new g(this));
    }
}
