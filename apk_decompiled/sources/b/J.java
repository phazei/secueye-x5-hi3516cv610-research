package b;

import b.K;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class J implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ K.a.b f2102a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ K.a f2103b;

    public J(K.a aVar, K.a.b bVar) {
        this.f2103b = aVar;
        this.f2102a = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        Runnable runnable = this.f2102a.f2114c;
        if (runnable != null) {
            runnable.run();
        }
        this.f2103b.f2110c = null;
        this.f2103b.a(this.f2102a.f2115d);
    }
}
