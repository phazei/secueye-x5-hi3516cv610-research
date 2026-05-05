package anet.channel;

import anet.channel.entity.EventCb;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.StrategyCenter;
import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class g implements EventCb {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Session f1754a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ SessionRequest f1755b;

    g(SessionRequest sessionRequest, Session session) {
        this.f1755b = sessionRequest;
        this.f1754a = session;
    }

    @Override // anet.channel.entity.EventCb
    public void onEvent(Session session, int i, anet.channel.entity.b bVar) {
        ALog.d("awcn.SessionRequest", "Receive session event", null, "eventType", Integer.valueOf(i));
        ConnEvent connEvent = new ConnEvent();
        if (i == 512) {
            connEvent.isSuccess = true;
        }
        if (this.f1755b.f1645c != null) {
            connEvent.isAccs = this.f1755b.f1645c.isAccs;
        }
        StrategyCenter.getInstance().notifyConnEvent(this.f1754a.getRealHost(), this.f1754a.getConnStrategy(), connEvent);
    }
}
