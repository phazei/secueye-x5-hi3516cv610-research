package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.fastjson.JSON;
import datasource.MeshConfigCallback;
import datasource.bean.IotDevice;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class U implements MeshConfigCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IotDevice f1234a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ MeshService f1235b;

    public U(MeshService meshService, IotDevice iotDevice) {
        this.f1235b = meshService;
        this.f1234a = iotDevice;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "getInfoByAuthInfo request success");
        this.f1235b.mConnectToMeshNetwork = true;
        this.f1235b.sendBroadcastIsConnected(true, null);
        this.f1235b.mHandler.removeCallbacks(this.f1235b.mProvisionTimeout);
        this.f1235b.sendBroadcastBindState(1, JSON.toJSONString(this.f1234a));
        this.f1235b.configProxyFilter();
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "getInfoByAuthInfo request failed, errorMessage: " + str2);
        this.f1235b.sendBroadcastBindState(-1, str2);
    }
}
