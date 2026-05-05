package anet.channel.strategy;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class i implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ g f1895a;

    i(g gVar) {
        this.f1895a = gVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1895a.a()) {
            return;
        }
        this.f1895a.f1891b.c();
    }
}
