package a.a.a.a.b;

import android.os.ParcelUuid;
import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class E implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1200a;

    public E(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1200a = deviceProvisioningWorker;
    }

    @Override // java.lang.Runnable
    public void run() {
        byte[] serviceData = this.f1200a.r.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID));
        if (this.f1200a.z == null) {
            this.f1200a.z = new UnprovisionedMeshNodeData(serviceData);
        }
        this.f1200a.e.a(this.f1200a.q.getAddress(), this.f1200a.q.getName(), serviceData, this.f1200a.z, this.f1200a.P);
        this.f1200a.b("identifyNode");
    }
}
