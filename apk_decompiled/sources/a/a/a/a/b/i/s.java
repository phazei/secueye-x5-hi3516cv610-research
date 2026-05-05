package a.a.a.a.b.i;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;

/* JADX INFO: compiled from: FastProvisionV2Worker.java */
/* JADX INFO: loaded from: classes.dex */
public class s implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ u f1437a;

    public s(u uVar) {
        this.f1437a = uVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (2 > this.f1437a.q) {
            this.f1437a.d();
            this.f1437a.n.postDelayed(this.f1437a.p, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
        } else {
            this.f1437a.f1442d.onProvisioningComplete(this.f1437a.h, null);
            this.f1437a.q = 0;
        }
    }
}
