package anet.channel.session;

import anet.channel.RequestCb;
import anet.channel.request.Request;
import anet.channel.session.b;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Request f1822a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ d f1823b;

    e(d dVar, Request request) {
        this.f1823b = dVar;
        this.f1822a = request;
    }

    @Override // java.lang.Runnable
    public void run() {
        b.a aVarA = b.a(this.f1822a, (RequestCb) null);
        if (aVarA.f1817a > 0) {
            this.f1823b.notifyStatus(4, new anet.channel.entity.b(1));
        } else {
            this.f1823b.handleCallbacks(256, new anet.channel.entity.b(256, aVarA.f1817a, "Http connect fail"));
        }
    }
}
