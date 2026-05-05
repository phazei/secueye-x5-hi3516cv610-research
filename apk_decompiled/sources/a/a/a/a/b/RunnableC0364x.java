package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;

/* JADX INFO: renamed from: a.a.a.a.b.x, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0364x implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1555a;

    public RunnableC0364x(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1555a = deviceProvisioningWorker;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f1555a.a(false, -31, 0, "no proactive reporting was received from the device");
    }
}
