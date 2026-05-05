package b.f;

/* JADX INFO: compiled from: LowerTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ e f2171a;

    public b(e eVar) {
        this.f2171a = eVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2171a.mLowerTransportLayerCallbacks.onIncompleteTimerExpired();
        this.f2171a.mIncompleteTimerStarted = false;
    }
}
