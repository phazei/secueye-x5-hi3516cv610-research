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
import datasource.bean.ServerConfirmation;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: renamed from: a.a.a.a.b.h, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0333h implements MeshConfigCallback<ServerConfirmation> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ InterfaceC0367a.b f1330a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedMeshNodeData f1331b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1332c;

    public C0333h(DeviceProvisioningWorker deviceProvisioningWorker, InterfaceC0367a.b bVar, UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.f1332c = deviceProvisioningWorker;
        this.f1330a = bVar;
        this.f1331b = unprovisionedMeshNodeData;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(ServerConfirmation serverConfirmation) {
        a.a.a.a.b.m.a.a(this.f1332c.f2789b, "provisionConfirm request success");
        if (serverConfirmation == null || this.f1330a == null) {
            return;
        }
        String serverConfirmation2 = serverConfirmation.getServerConfirmation();
        if (!this.f1331b.isFastProvisionMesh()) {
            serverConfirmation2 = "0305" + serverConfirmation.getServerConfirmation();
        }
        a.a.a.a.b.m.a.a(this.f1332c.f2789b, "provisionConfirm value: " + serverConfirmation2);
        this.f1330a.generate(serverConfirmation2);
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        String str3;
        a.a.a.a.b.m.a.b(this.f1332c.f2789b, "provisionConfirm request failed, errorMessage: " + str2);
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_CONFIGURATION_STATE, MeshNodeStatus.REQUEST_FAILED.getState());
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.PROVISION_CONFIRM_REQUEST_ERROR;
        intent.putExtra(Utils.EXTRA_REQUEST_FAIL_MSG, meshUtConst$MeshErrorEnum.getErrorMsg() + " : " + str2);
        if (this.f1332c.z == null) {
            str3 = "";
        } else {
            str3 = this.f1332c.z.getProductId() + "";
        }
        a.a.a.a.b.m.b.a("ALSMesh", "ble", str3, false, this.f1332c.r.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID)), "", 0L, meshUtConst$MeshErrorEnum.getErrorCode(), meshUtConst$MeshErrorEnum.getErrorMsg(), str, str2);
        LocalBroadcastManager.getInstance(this.f1332c.f2790c).sendBroadcast(intent);
    }
}
