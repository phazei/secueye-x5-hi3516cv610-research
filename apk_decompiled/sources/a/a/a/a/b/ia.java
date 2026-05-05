package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class ia implements IActionListener<String> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1450a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1451b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ byte[] f1452c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ MeshService.b f1453d;

    public ia(MeshService.b bVar, IActionListener iActionListener, ProvisionedMeshNode provisionedMeshNode, byte[] bArr) {
        this.f1453d = bVar;
        this.f1450a = iActionListener;
        this.f1451b = provisionedMeshNode;
        this.f1452c = bArr;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(String str) {
        Utils.notifySuccess((IActionListener<String>) this.f1450a, str);
        ProvisionedMeshNode provisionedMeshNode = this.f1451b;
        if (provisionedMeshNode != null) {
            this.f1453d.a(provisionedMeshNode.getUnicastAddress(), 13936641, this.f1452c);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        Utils.notifyFailed(this.f1450a, i, str);
        ProvisionedMeshNode provisionedMeshNode = this.f1451b;
        if (provisionedMeshNode != null) {
            this.f1453d.a(provisionedMeshNode.getUnicastAddress(), 13936641, this.f1452c);
        }
    }
}
