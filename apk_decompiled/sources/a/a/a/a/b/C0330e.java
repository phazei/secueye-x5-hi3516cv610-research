package a.a.a.a.b;

import android.content.Context;
import android.os.Build;
import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;
import datasource.MeshConfigCallback;
import datasource.bean.ProvisionInfo;
import datasource.bean.SigmeshKey;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: renamed from: a.a.a.a.b.e, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0330e implements MeshConfigCallback<ProvisionInfo> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedMeshNodeData f1319a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f1320b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1321c;

    public C0330e(DeviceProvisioningWorker deviceProvisioningWorker, UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr) {
        this.f1321c = deviceProvisioningWorker;
        this.f1319a = unprovisionedMeshNodeData;
        this.f1320b = bArr;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(ProvisionInfo provisionInfo) {
        Integer num;
        SigmeshKey sigmeshKey;
        a.a.a.a.b.m.a.a(this.f1321c.f2789b, "getProvisionInfo request success");
        if (provisionInfo == null) {
            MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.NULL_PROVISION_INFO_ERROR;
            this.f1321c.a(meshUtConst$MeshErrorEnum, meshUtConst$MeshErrorEnum.getErrorMsg());
            return;
        }
        if (provisionInfo.getPrimaryUnicastAddress() == null || provisionInfo.getNetKeyIndexes() == null) {
            return;
        }
        this.f1321c.m.e(provisionInfo.getPrimaryUnicastAddress().intValue());
        this.f1321c.v = provisionInfo.getNetKeyIndexes();
        if (this.f1321c.v == null || (num = (Integer) this.f1321c.v.get(0)) == null || (sigmeshKey = (SigmeshKey) this.f1321c.t.get(num.intValue())) == null || sigmeshKey.getProvisionNetKey() == null) {
            return;
        }
        a.a.a.a.b.m.a.a(this.f1321c.f2789b, "Update provisioning setttings");
        this.f1321c.m.a(sigmeshKey.getProvisionNetKey().getNetKey());
        this.f1321c.m.d(sigmeshKey.getProvisionNetKey().getNetKeyIndex());
        if (this.f1319a.isFastProvisionMesh() && Build.VERSION.SDK_INT >= 21 && this.f1321c.P != null) {
            a.a.a.a.b.i.J j = this.f1321c.P;
            Context context = this.f1321c.f2790c;
            DeviceProvisioningWorker deviceProvisioningWorker = this.f1321c;
            b.s sVar = deviceProvisioningWorker.m;
            DeviceProvisioningWorker deviceProvisioningWorker2 = this.f1321c;
            j.a(context, deviceProvisioningWorker, sVar, deviceProvisioningWorker2, deviceProvisioningWorker2, deviceProvisioningWorker2);
        }
        this.f1321c.l = true;
        if (this.f1321c.k) {
            a.a.a.a.b.m.a.a(this.f1321c.f2789b, "identifyNode after provisioning info is ready");
            this.f1321c.e.a(this.f1321c.q.getAddress(), this.f1321c.q.getName(), this.f1320b, this.f1319a, this.f1321c.P);
            this.f1321c.b("identifyNode");
        }
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        a.a.a.a.b.m.a.b(this.f1321c.f2789b, "getProvisionInfo request failed, errorMessage: " + str2);
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.GET_PROVISION_REQUEST_ERROR;
        this.f1321c.a(meshUtConst$MeshErrorEnum, meshUtConst$MeshErrorEnum.getErrorMsg() + " : " + str2);
    }
}
