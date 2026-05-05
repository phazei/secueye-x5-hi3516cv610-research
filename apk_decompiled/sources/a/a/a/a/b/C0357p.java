package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import datasource.MeshConfigCallback;
import datasource.bean.IotDevice;

/* JADX INFO: renamed from: a.a.a.a.b.p, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0357p implements MeshConfigCallback<String> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IotDevice f1523a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ String f1524b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1525c;

    public C0357p(DeviceProvisioningWorker deviceProvisioningWorker, IotDevice iotDevice, String str) {
        this.f1525c = deviceProvisioningWorker;
        this.f1523a = iotDevice;
        this.f1524b = str;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(String str) {
        this.f1525c.a(this.f1523a, this.f1524b);
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.a(this.f1525c.f2789b, "getInfoByAuthInfo request failed, errorMessage: " + str2);
        this.f1525c.a(-1, str2);
    }
}
