package anet.channel.e;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.session.TnetSpdySession;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;
import java.util.List;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ List f1729a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ NetworkStatusHelper.NetworkStatus f1730b;

    e(List list, NetworkStatusHelper.NetworkStatus networkStatus) {
        this.f1729a = list;
        this.f1730b = networkStatus;
    }

    @Override // java.lang.Runnable
    public void run() {
        IConnStrategy iConnStrategy = (IConnStrategy) this.f1729a.get(0);
        TnetSpdySession tnetSpdySession = new TnetSpdySession(GlobalAppRuntimeInfo.getContext(), new anet.channel.entity.a("https://" + a.f1723b, "Http3Detect" + a.h.getAndIncrement(), a.b(iConnStrategy)));
        tnetSpdySession.registerEventcb(257, new f(this, iConnStrategy));
        tnetSpdySession.q.isCommitted = true;
        tnetSpdySession.connect();
    }
}
