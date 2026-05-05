package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;

/* JADX INFO: renamed from: a.a.a.a.b.u, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0361u implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1547a;

    public RunnableC0361u(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1547a = deviceProvisioningWorker;
    }

    @Override // java.lang.Runnable
    public void run() {
        a.a.a.a.b.m.a.c(this.f1547a.f2789b, "Execute state timeout task");
        this.f1547a.f2791d.read();
    }
}
