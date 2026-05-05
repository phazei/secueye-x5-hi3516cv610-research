package b;

import java.util.List;
import meshprovisioner.BaseMeshNode;

/* JADX INFO: renamed from: b.f, reason: case insensitive filesystem */
/* JADX INFO: compiled from: MeshManagerApi.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0372f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ List f2168a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ BaseMeshNode f2169b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ C0378l f2170c;

    public RunnableC0372f(C0378l c0378l, List list, BaseMeshNode baseMeshNode) {
        this.f2170c = c0378l;
        this.f2168a = list;
        this.f2169b = baseMeshNode;
    }

    @Override // java.lang.Runnable
    public void run() {
        for (byte[] bArr : this.f2168a) {
            try {
                Thread.sleep(150L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.f2170c.n.post(new RunnableC0371e(this, bArr));
        }
    }
}
