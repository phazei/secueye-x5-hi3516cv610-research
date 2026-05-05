package a.a.a.a.b;

import android.content.Intent;
import android.os.ParcelUuid;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import b.InterfaceC0367a;
import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.bean.MeshNodeStatus;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import datasource.MeshConfigCallback;
import meshprovisioner.states.UnprovisionedMeshNode;

/* JADX INFO: renamed from: a.a.a.a.b.i, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0334i implements MeshConfigCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ InterfaceC0367a.InterfaceC0176a f1338a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1339b;

    public C0334i(DeviceProvisioningWorker deviceProvisioningWorker, InterfaceC0367a.InterfaceC0176a interfaceC0176a) {
        this.f1339b = deviceProvisioningWorker;
        this.f1338a = interfaceC0176a;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a(this.f1339b.f2789b, "provisionAuth request success " + bool);
        if (bool != null && this.f1338a != null && (this.f1339b.i instanceof UnprovisionedMeshNode)) {
            this.f1338a.a((UnprovisionedMeshNode) this.f1339b.i, bool.booleanValue());
            return;
        }
        String str = this.f1339b.f2789b;
        StringBuilder sb = new StringBuilder();
        sb.append("callback is null ? ");
        sb.append(this.f1338a == null);
        sb.append(", mMeshNode == null ? ");
        sb.append(this.f1339b.i == null);
        sb.append(", mMeshNode instanceof UnprovisionedMeshNode ? ");
        sb.append(this.f1339b.i instanceof UnprovisionedMeshNode);
        a.a.a.a.b.m.a.b(str, sb.toString());
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        String str3;
        a.a.a.a.b.m.a.b(this.f1339b.f2789b, "provisionAuth request failed, errorMessage: " + str2);
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_CONFIGURATION_STATE, MeshNodeStatus.REQUEST_FAILED.getState());
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.PROVISION_AUTH_REQUEST_ERROR;
        intent.putExtra(Utils.EXTRA_REQUEST_FAIL_MSG, meshUtConst$MeshErrorEnum.getErrorMsg() + " : " + str2);
        if (this.f1339b.z == null) {
            str3 = "";
        } else {
            str3 = this.f1339b.z.getProductId() + "";
        }
        a.a.a.a.b.m.b.a("ALSMesh", "ble", str3, false, this.f1339b.r.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID)), "", 0L, meshUtConst$MeshErrorEnum.getErrorCode(), meshUtConst$MeshErrorEnum.getErrorMsg(), str, str2);
        LocalBroadcastManager.getInstance(this.f1339b.f2790c).sendBroadcast(intent);
    }
}
