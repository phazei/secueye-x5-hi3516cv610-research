package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: renamed from: a.a.a.a.b.i.k, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0345k implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1426a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1427b;

    public C0345k(FastProvisionManager fastProvisionManager, ProvisionedMeshNode provisionedMeshNode) {
        this.f1427b = fastProvisionManager;
        this.f1426a = provisionedMeshNode;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        this.f1427b.onConfigInfoReceived(this.f1426a);
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1427b.onProvisionFailed(i, str);
    }
}
