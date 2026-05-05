package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: renamed from: a.a.a.a.b.i.l, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0346l implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1428a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1429b;

    public C0346l(FastProvisionManager fastProvisionManager, ProvisionedMeshNode provisionedMeshNode) {
        this.f1429b = fastProvisionManager;
        this.f1428a = provisionedMeshNode;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        this.f1429b.onAddAppKeyMsgSend(this.f1428a);
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1429b.onProvisionFailed(i, str);
    }
}
