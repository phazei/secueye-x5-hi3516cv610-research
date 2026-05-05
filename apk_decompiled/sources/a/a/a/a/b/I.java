package a.a.a.a.b;

import android.os.Build;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;
import datasource.MeshConfigCallback;
import datasource.bean.ProvisionInfo;
import datasource.bean.SigmeshKey;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class I implements MeshConfigCallback<ProvisionInfo> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedMeshNodeData f1207a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f1208b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ MeshService f1209c;

    public I(MeshService meshService, UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr) {
        this.f1209c = meshService;
        this.f1207a = unprovisionedMeshNodeData;
        this.f1208b = bArr;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(ProvisionInfo provisionInfo) {
        Integer num;
        SigmeshKey sigmeshKey;
        a.a.a.a.b.m.a.a(MeshService.TAG, "getProvisionInfo request success");
        if (provisionInfo == null) {
            MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.NULL_PROVISION_INFO_ERROR;
            this.f1209c.handleProvisionFailed(meshUtConst$MeshErrorEnum, meshUtConst$MeshErrorEnum.getErrorMsg());
            return;
        }
        if (provisionInfo.getPrimaryUnicastAddress() == null || provisionInfo.getNetKeyIndexes() == null) {
            return;
        }
        this.f1209c.mProvisioningSettings.e(provisionInfo.getPrimaryUnicastAddress().intValue());
        this.f1209c.mNetKeyIndexes = provisionInfo.getNetKeyIndexes();
        if (this.f1209c.mNetKeyIndexes == null || (num = (Integer) this.f1209c.mNetKeyIndexes.get(0)) == null || (sigmeshKey = (SigmeshKey) this.f1209c.mSigmeshKeys.get(num.intValue())) == null || sigmeshKey.getProvisionNetKey() == null) {
            return;
        }
        a.a.a.a.b.m.a.a(MeshService.TAG, "Update provisioning setttings");
        this.f1209c.mProvisioningSettings.a(sigmeshKey.getProvisionNetKey().getNetKey());
        this.f1209c.mProvisioningSettings.d(sigmeshKey.getProvisionNetKey().getNetKeyIndex());
        if (this.f1207a.isFastProvisionMesh() && Build.VERSION.SDK_INT >= 21 && this.f1209c.mFastProvisionWorker != null) {
            a.a.a.a.b.i.J j = this.f1209c.mFastProvisionWorker;
            MeshService meshService = this.f1209c;
            b.s sVar = meshService.mProvisioningSettings;
            MeshService meshService2 = this.f1209c;
            j.a(meshService, meshService, sVar, meshService2, meshService2, meshService2);
        }
        this.f1209c.mProvisionInfoReady = true;
        if (this.f1209c.mDeviceIsReadyInProvisioningStep) {
            a.a.a.a.b.m.a.a(MeshService.TAG, "identifyNode after provisioning info is ready");
            this.f1209c.mMeshManagerApi.a(this.f1209c.mBluetoothDevice.getAddress(), this.f1209c.mBluetoothDevice.getName(), this.f1208b, this.f1207a, this.f1209c.mFastProvisionWorker);
            this.f1209c.sendBroadcastConnectionState("identifyNode");
        }
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.b(MeshService.TAG, "getProvisionInfo request failed, errorMessage: " + str2);
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.GET_PROVISION_REQUEST_ERROR;
        this.f1209c.handleProvisionFailed(meshUtConst$MeshErrorEnum, meshUtConst$MeshErrorEnum.getErrorMsg() + " : " + str2);
    }
}
