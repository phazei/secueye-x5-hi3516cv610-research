package anet.channel.e;

import anet.channel.Session;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.entity.EventCb;
import anet.channel.statist.Http3DetectStat;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class f implements EventCb {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ IConnStrategy f1731a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ e f1732b;

    f(e eVar, IConnStrategy iConnStrategy) {
        this.f1732b = eVar;
        this.f1731a = iConnStrategy;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r5v3 */
    @Override // anet.channel.entity.EventCb
    public void onEvent(Session session, int i, anet.channel.entity.b bVar) {
        ?? r5 = i != 1 ? 0 : 1;
        a.f1722a.a(NetworkStatusHelper.getUniqueId(this.f1732b.f1730b), r5);
        session.close(false);
        Http3DetectStat http3DetectStat = new Http3DetectStat(a.f1723b, this.f1731a);
        http3DetectStat.ret = r5;
        AppMonitor.getInstance().commitStat(http3DetectStat);
    }
}
