package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: renamed from: a.a.a.a.b.k, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0352k implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1465a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1466b;

    public RunnableC0352k(DeviceProvisioningWorker deviceProvisioningWorker, ProvisionedMeshNode provisionedMeshNode) {
        this.f1466b = deviceProvisioningWorker;
        this.f1465a = provisionedMeshNode;
    }

    @Override // java.lang.Runnable
    public void run() {
        String str = (String) this.f1466b.o.poll();
        Integer num = (Integer) this.f1466b.n.poll();
        if (str != null) {
            if (num == null) {
                num = 0;
            }
            a.a.a.a.b.m.a.c(this.f1466b.f2789b, "try to add app key: appKeyIndex = " + num + ", mAppKey = " + str);
            this.f1466b.e.a(this.f1465a, num.intValue(), str);
        }
        a.a.a.a.b.m.a.a(this.f1466b.f2789b, "addAppKey");
    }
}
