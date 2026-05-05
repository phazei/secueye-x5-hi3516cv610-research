package anet.channel;

import anet.channel.entity.EventCb;
import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f1665a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ anet.channel.entity.b f1666b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ Session f1667c;

    b(Session session, int i, anet.channel.entity.b bVar) {
        this.f1667c = session;
        this.f1665a = i;
        this.f1666b = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.f1667c.f1633b != null) {
                for (EventCb eventCb : this.f1667c.f1633b.keySet()) {
                    if (eventCb != null && (this.f1667c.f1633b.get(eventCb).intValue() & this.f1665a) != 0) {
                        try {
                            eventCb.onEvent(this.f1667c, this.f1665a, this.f1666b);
                        } catch (Exception e) {
                            ALog.e("awcn.Session", e.toString(), this.f1667c.p, new Object[0]);
                        }
                    }
                }
            }
        } catch (Exception e2) {
            ALog.e("awcn.Session", "handleCallbacks", this.f1667c.p, e2, new Object[0]);
        }
    }
}
