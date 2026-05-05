package anet.channel;

import anet.channel.SessionRequest;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class i implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Session f1768a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ SessionRequest.a f1769b;

    i(SessionRequest.a aVar, Session session) {
        this.f1769b = aVar;
        this.f1768a = session;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            SessionRequest.this.a(this.f1769b.f1649c, this.f1768a.getConnType().getType(), anet.channel.util.i.a(SessionRequest.this.f1643a.f1639c), (SessionGetCallback) null, 0L);
        } catch (Exception unused) {
        }
    }
}
