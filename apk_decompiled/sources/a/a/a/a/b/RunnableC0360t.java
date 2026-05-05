package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;

/* JADX INFO: renamed from: a.a.a.a.b.t, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0360t implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1544a;

    public RunnableC0360t(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1544a = deviceProvisioningWorker;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1544a.R.get()) {
            a.a.a.a.b.m.a.d(this.f1544a.f2789b, "The connect semaphore is not released as expected");
            this.f1544a.o();
            this.f1544a.p();
            MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.GATT_CONNECT_TIMEOUT;
            this.f1544a.a(meshUtConst$MeshErrorEnum, meshUtConst$MeshErrorEnum.getErrorMsg());
        }
    }
}
