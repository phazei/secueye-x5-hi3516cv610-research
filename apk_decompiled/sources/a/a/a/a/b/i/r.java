package a.a.a.a.b.i;

/* JADX INFO: compiled from: FastProvisionV2Worker.java */
/* JADX INFO: loaded from: classes.dex */
public class r implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Class f1435a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ u f1436b;

    public r(u uVar, Class cls) {
        this.f1436b = uVar;
        this.f1435a = cls;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1435a.isInstance(this.f1436b.m)) {
            return;
        }
        a.a.a.a.b.m.a.d(this.f1436b.f1439a, "wait " + this.f1435a.getSimpleName() + " resp timeout");
        u uVar = this.f1436b;
        uVar.onProvisioningFailed(uVar.g, -63, "wait resp timeout, invalid connection may happen");
    }
}
