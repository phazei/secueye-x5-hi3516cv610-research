package a.a.a.a.b.i;

import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: compiled from: FastProvisionWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class B implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedMeshNodeData f1342a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ String f1343b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ J f1344c;

    public B(J j, UnprovisionedMeshNodeData unprovisionedMeshNodeData, String str) {
        this.f1344c = j;
        this.f1342a = unprovisionedMeshNodeData;
        this.f1343b = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f1344c.onReceiveConfirmationFromCloud(this.f1342a, this.f1343b);
    }
}
