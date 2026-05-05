package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: renamed from: a.a.a.a.b.i.h, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0342h implements IActionListener<byte[]> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedMeshNodeData f1422a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1423b;

    public C0342h(FastProvisionManager fastProvisionManager, UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.f1423b = fastProvisionManager;
        this.f1422a = unprovisionedMeshNodeData;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(byte[] bArr) {
        a.a.a.a.b.m.a.c(FastProvisionManager.TAG, "broadcast random success");
        this.f1423b.onBroadcastingRandoms(this.f1422a);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1423b.onProvisionFailed(i, str);
    }
}
