package a.a.a.a.b.i;

/* JADX INFO: compiled from: FastProvisionWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class w implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ J f1446a;

    public w(J j) {
        this.f1446a = j;
    }

    @Override // java.lang.Runnable
    public void run() {
        a.a.a.a.b.m.a.a(this.f1446a.f1354a, "provision success, delay stop scan");
        this.f1446a.m();
    }
}
