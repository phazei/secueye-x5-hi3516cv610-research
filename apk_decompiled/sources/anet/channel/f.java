package anet.channel;

import anet.channel.SessionRequest;
import anet.channel.entity.EventCb;
import anet.channel.util.ALog;
import io.netty.handler.codec.rtsp.RtspHeaders;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class f implements EventCb {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ SessionRequest.IConnCb f1744a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ long f1745b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ SessionRequest f1746c;

    f(SessionRequest sessionRequest, SessionRequest.IConnCb iConnCb, long j) {
        this.f1746c = sessionRequest;
        this.f1744a = iConnCb;
        this.f1745b = j;
    }

    @Override // anet.channel.entity.EventCb
    public void onEvent(Session session, int i, anet.channel.entity.b bVar) {
        if (session == null) {
            return;
        }
        int i2 = bVar == null ? 0 : bVar.f1739b;
        String str = bVar == null ? "" : bVar.f1740c;
        if (i == 2) {
            ALog.d("awcn.SessionRequest", null, session != null ? session.p : null, RtspHeaders.Names.SESSION, session, "EventType", Integer.valueOf(i), "Event", bVar);
            this.f1746c.a(session, i2, str);
            if (this.f1746c.f1644b.c(this.f1746c, session)) {
                this.f1744a.onDisConnect(session, this.f1745b, i);
                return;
            } else {
                this.f1744a.onFailed(session, this.f1745b, i, i2);
                return;
            }
        }
        if (i == 256) {
            ALog.d("awcn.SessionRequest", null, session != null ? session.p : null, RtspHeaders.Names.SESSION, session, "EventType", Integer.valueOf(i), "Event", bVar);
            this.f1744a.onFailed(session, this.f1745b, i, i2);
        } else {
            if (i != 512) {
                return;
            }
            ALog.d("awcn.SessionRequest", null, session != null ? session.p : null, RtspHeaders.Names.SESSION, session, "EventType", Integer.valueOf(i), "Event", bVar);
            this.f1746c.a(session, 0, (String) null);
            this.f1744a.onSuccess(session, this.f1745b);
        }
    }
}
