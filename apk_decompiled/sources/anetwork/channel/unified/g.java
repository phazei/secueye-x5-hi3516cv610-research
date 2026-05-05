package anetwork.channel.unified;

import anet.channel.Session;
import anet.channel.SessionCenter;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.HttpUrl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class g implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ SessionCenter f2067a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ HttpUrl f2068b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ RequestStatistic f2069c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ HttpUrl f2070d;
    final /* synthetic */ boolean e;
    final /* synthetic */ e f;

    g(e eVar, SessionCenter sessionCenter, HttpUrl httpUrl, RequestStatistic requestStatistic, HttpUrl httpUrl2, boolean z) {
        this.f = eVar;
        this.f2067a = sessionCenter;
        this.f2068b = httpUrl;
        this.f2069c = requestStatistic;
        this.f2070d = httpUrl2;
        this.e = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        Session session = this.f2067a.get(this.f2068b, anet.channel.entity.c.f1741a, 3000L);
        this.f2069c.connWaitTime = System.currentTimeMillis() - jCurrentTimeMillis;
        this.f2069c.spdyRequestSend = session != null;
        Session sessionA = this.f.a(session, this.f2067a, this.f2070d, this.e);
        e eVar = this.f;
        eVar.a(sessionA, eVar.f2059a.f2078a.a());
    }
}
