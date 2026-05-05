package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.fastjson.JSON;
import datasource.MeshConfigCallback;
import datasource.bean.IotDevice;

/* JADX INFO: renamed from: a.a.a.a.b.q, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0358q implements MeshConfigCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IotDevice f1530a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1531b;

    public C0358q(DeviceProvisioningWorker deviceProvisioningWorker, IotDevice iotDevice) {
        this.f1531b = deviceProvisioningWorker;
        this.f1530a = iotDevice;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a(this.f1531b.f2789b, "getInfoByAuthInfo request success");
        this.f1531b.O = true;
        this.f1531b.a(true, (String) null);
        this.f1531b.s.removeCallbacks(this.f1531b.ca);
        this.f1531b.a(1, JSON.toJSONString(this.f1530a));
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.a(this.f1531b.f2789b, "getInfoByAuthInfo request failed, errorMessage: " + str2);
        this.f1531b.a(-1, str2);
    }
}
