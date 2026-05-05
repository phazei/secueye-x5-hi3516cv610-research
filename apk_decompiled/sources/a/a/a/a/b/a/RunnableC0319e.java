package a.a.a.a.b.a;

/* JADX INFO: renamed from: a.a.a.a.b.a.e, reason: case insensitive filesystem */
/* JADX INFO: compiled from: RequestQueue.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0319e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0321g f1256a;

    public RunnableC0319e(C0321g c0321g) {
        this.f1256a = c0321g;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1256a.f > 0) {
            try {
                Thread.sleep(this.f1256a.f);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        a.a.a.a.b.m.a.a(C0321g.f1259a, "Real Dispatcher");
        this.f1256a.f1262d.c();
    }
}
