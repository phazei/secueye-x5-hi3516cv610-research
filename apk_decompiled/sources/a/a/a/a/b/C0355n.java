package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import datasource.MeshConfigCallback;

/* JADX INFO: renamed from: a.a.a.a.b.n, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0355n implements MeshConfigCallback<String> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1516a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1517b;

    public C0355n(DeviceProvisioningWorker deviceProvisioningWorker, int i) {
        this.f1517b = deviceProvisioningWorker;
        this.f1516a = i;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(String str) {
        a.a.a.a.b.m.a.a(this.f1517b.f2789b, "reportDevicesStatus request success");
        a.a.a.a.b.l.c.a(this.f1516a, 1, true);
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.b(this.f1517b.f2789b, "reportDevicesStatus request failed, errorMessage: " + str2);
        a.a.a.a.b.l.c.a(this.f1516a, 0, true);
    }
}
