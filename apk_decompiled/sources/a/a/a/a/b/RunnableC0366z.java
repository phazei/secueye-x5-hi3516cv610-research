package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.ut.UtError;

/* JADX INFO: renamed from: a.a.a.a.b.z, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0366z implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1559a;

    public RunnableC0366z(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1559a = deviceProvisioningWorker;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (MeshDeviceInfoManager.getInstance().isLowCostDeviceExist()) {
            a.a.a.a.b.m.a.a(this.f1559a.f2789b, "scan timeout");
            return;
        }
        if (this.f1559a.J) {
            this.f1559a.a(false, UtError.MESH_SCAN_TIMEOUT.getMsg());
        }
        this.f1559a.s();
    }
}
