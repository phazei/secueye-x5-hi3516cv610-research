package anet.channel.detect;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f1695a;

    b(a aVar) {
        this.f1695a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f1695a.f1694a.e.clear();
        this.f1695a.f1694a.f1684a = 0L;
    }
}
