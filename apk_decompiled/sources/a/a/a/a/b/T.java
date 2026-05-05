package a.a.a.a.b;

import android.os.Build;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.fastjson.JSON;
import datasource.MeshConfigCallback;
import datasource.bean.IotDevice;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class T implements MeshConfigCallback<String> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IotDevice f1232a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ MeshService f1233b;

    public T(MeshService meshService, IotDevice iotDevice) {
        this.f1233b = meshService;
        this.f1232a = iotDevice;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(String str) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "getInfoByAuthInfo request success");
        if (this.f1233b.mCurrentProvisionMeshNode != null && !this.f1233b.mCurrentProvisionMeshNode.getSupportFastProvision()) {
            this.f1233b.mConnectToMeshNetwork = true;
            this.f1233b.sendBroadcastIsConnected(true, null);
        }
        this.f1233b.mHandler.removeCallbacks(this.f1233b.mProvisionTimeout);
        this.f1233b.sendBroadcastBindState(1, JSON.toJSONString(this.f1232a));
        this.f1233b.configProxyFilter();
        if (Build.VERSION.SDK_INT >= 21 && this.f1233b.mFastProvisionWorker != null) {
            this.f1233b.mFastProvisionWorker.l();
        }
        if (this.f1233b.mCurrentProvisionMeshNode == null || this.f1233b.mUnprovisionedMeshNodeData == null) {
            return;
        }
        try {
            a.a.a.a.b.e.a.a(String.valueOf(this.f1233b.mUnprovisionedMeshNodeData.getProductId()), MeshParserUtils.bytesToHex(this.f1233b.mUnprovisionedMeshNodeData.getDeviceUuid(), false), this.f1233b.mUnprovisionedMeshNodeData.getDeviceMac(), this.f1233b.mCurrentProvisionMeshNode.getUnicastAddressInt(), MeshParserUtils.bytesToHex(this.f1233b.mCurrentProvisionMeshNode.getDeviceKey(), false));
        } catch (Exception unused) {
        }
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "getInfoByAuthInfo request failed, errorMessage: " + str2);
        this.f1233b.sendBroadcastBindState(-1, str2);
    }
}
