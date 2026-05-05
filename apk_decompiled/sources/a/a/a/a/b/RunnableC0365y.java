package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;

/* JADX INFO: renamed from: a.a.a.a.b.y, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0365y implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1557a;

    public RunnableC0365y(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1557a = deviceProvisioningWorker;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1557a.J) {
            return;
        }
        this.f1557a.q();
    }
}
