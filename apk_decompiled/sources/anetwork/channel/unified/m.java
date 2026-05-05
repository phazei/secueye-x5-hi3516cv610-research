package anetwork.channel.unified;

import anetwork.channel.unified.k.a;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class m implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2088a;

    m(k kVar) {
        this.f2088a = kVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        k kVar = this.f2088a;
        kVar.new a(0, kVar.f2082a.f2078a.a(), this.f2088a.f2082a.f2079b).proceed(this.f2088a.f2082a.f2078a.a(), this.f2088a.f2082a.f2079b);
    }
}
