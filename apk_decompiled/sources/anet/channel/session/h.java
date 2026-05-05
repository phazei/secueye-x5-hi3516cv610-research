package anet.channel.session;

import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.StrategyCenter;
import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class h implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ TnetSpdySession f1829a;

    h(TnetSpdySession tnetSpdySession) {
        this.f1829a = tnetSpdySession;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1829a.y) {
            ALog.e("awcn.TnetSpdySession", "send msg time out!", this.f1829a.p, "pingUnRcv:", Boolean.valueOf(this.f1829a.y));
            try {
                this.f1829a.handleCallbacks(2048, null);
                if (this.f1829a.q != null) {
                    this.f1829a.q.closeReason = "ping time out";
                }
                ConnEvent connEvent = new ConnEvent();
                connEvent.isSuccess = false;
                connEvent.isAccs = this.f1829a.I;
                StrategyCenter.getInstance().notifyConnEvent(this.f1829a.f1635d, this.f1829a.k, connEvent);
                this.f1829a.close(true);
            } catch (Exception unused) {
            }
        }
    }
}
