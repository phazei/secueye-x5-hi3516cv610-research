package a.a.a.a.b.i.c;

/* JADX INFO: compiled from: TinyMeshGattTransportLayerV2.java */
/* JADX INFO: loaded from: classes.dex */
public class q implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ r f1412a;

    public q(r rVar) {
        this.f1412a = rVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        a.a.a.a.b.m.a.d(this.f1412a.f1413a, "re-discovery service");
        this.f1412a.f1414b.discoveryServices(false);
    }
}
