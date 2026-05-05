package a.a.a.a.b;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class ba implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ca f1302a;

    public ba(ca caVar) {
        this.f1302a = caVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f1302a.f1312a.isRetry = true;
        this.f1302a.f1312a.init();
    }
}
