package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: compiled from: FastProvisionWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class A implements IActionListener<byte[]> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedMeshNodeData f1340a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ J f1341b;

    public A(J j, UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.f1341b = j;
        this.f1340a = unprovisionedMeshNodeData;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(byte[] bArr) {
        a.a.a.a.b.m.a.c(this.f1341b.f1354a, "broadcast random success");
        this.f1341b.onBroadcastingRandoms(this.f1340a);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1341b.onProvisionFailed(i, str);
    }
}
