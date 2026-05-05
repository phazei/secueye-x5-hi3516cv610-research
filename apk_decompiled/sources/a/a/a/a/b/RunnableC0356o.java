package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;

/* JADX INFO: renamed from: a.a.a.a.b.o, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0356o implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1522a;

    public RunnableC0356o(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1522a = deviceProvisioningWorker;
    }

    @Override // java.lang.Runnable
    public void run() {
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.TIMEOUT_ERROR;
        this.f1522a.a(meshUtConst$MeshErrorEnum, meshUtConst$MeshErrorEnum.getErrorMsg());
    }
}
