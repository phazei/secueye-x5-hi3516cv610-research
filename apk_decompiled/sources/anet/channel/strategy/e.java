package anet.channel.strategy;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f1886a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ StrategyInfoHolder f1887b;

    e(StrategyInfoHolder strategyInfoHolder, String str) {
        this.f1887b = strategyInfoHolder;
        this.f1886a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f1887b.a(this.f1886a, true);
    }
}
