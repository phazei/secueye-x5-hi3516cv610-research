package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.provision.callback.AliMeshProvisioningFrameworkStatusCallbacks;
import datasource.bean.DeviceStatus;
import java.util.List;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;

/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C implements AliMeshProvisioningFrameworkStatusCallbacks {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1198a;

    public C(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1198a = deviceProvisioningWorker;
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.AliMeshProvisioningFrameworkStatusCallbacks
    public void onConfigurationComplete(ProvisionedMeshNode provisionedMeshNode) {
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.AliMeshProvisioningFrameworkStatusCallbacks
    public void onProvisioningComplete(ProvisionedMeshNode provisionedMeshNode, List<DeviceStatus> list) {
        a.a.a.a.b.i.c.a aVarB = this.f1198a.X.b();
        if (aVarB instanceof a.a.a.a.b.i.c.r) {
            BleMeshManager bleMeshManagerD = ((a.a.a.a.b.i.c.r) aVarB).d();
            if (this.f1198a.f2791d != null) {
                bleMeshManagerD.setProvisioningComplete(true);
                bleMeshManagerD.setGattCallbacks(this.f1198a);
                this.f1198a.f2791d = bleMeshManagerD;
            }
        }
        this.f1198a.i = provisionedMeshNode;
        if (list != null && list.size() > 0) {
            this.f1198a.U.a(list);
        }
        this.f1198a.a(provisionedMeshNode.getUnicastAddress(), (list == null || list.size() <= 0) ? null : list.get(0));
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.AliMeshProvisioningFrameworkStatusCallbacks
    public void onProvisioningFailed(BaseMeshNode baseMeshNode, int i) {
        this.f1198a.onProvisioningFailed((UnprovisionedMeshNode) baseMeshNode, i);
    }
}
