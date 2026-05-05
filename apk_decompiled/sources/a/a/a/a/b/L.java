package a.a.a.a.b;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import b.InterfaceC0367a;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.bean.MeshNodeStatus;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import datasource.MeshConfigCallback;
import datasource.bean.ServerConfirmation;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class L implements MeshConfigCallback<ServerConfirmation> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ InterfaceC0367a.b f1213a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedMeshNodeData f1214b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ MeshService f1215c;

    public L(MeshService meshService, InterfaceC0367a.b bVar, UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.f1215c = meshService;
        this.f1213a = bVar;
        this.f1214b = unprovisionedMeshNodeData;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(ServerConfirmation serverConfirmation) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "provisionConfirm request success");
        if (serverConfirmation == null || this.f1213a == null) {
            return;
        }
        String serverConfirmation2 = serverConfirmation.getServerConfirmation();
        if (!this.f1214b.isFastProvisionMesh()) {
            serverConfirmation2 = "0305" + serverConfirmation.getServerConfirmation();
        }
        a.a.a.a.b.m.a.a(MeshService.TAG, "provisionConfirm value: " + serverConfirmation2);
        this.f1213a.generate(serverConfirmation2);
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        String str3;
        a.a.a.a.b.m.a.b(MeshService.TAG, "provisionConfirm request failed, errorMessage: " + str2);
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_CONFIGURATION_STATE, MeshNodeStatus.REQUEST_FAILED.getState());
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.PROVISION_CONFIRM_REQUEST_ERROR;
        intent.putExtra(Utils.EXTRA_REQUEST_FAIL_MSG, meshUtConst$MeshErrorEnum.getErrorMsg() + " : " + str2);
        if (this.f1215c.mUnprovisionedMeshNodeData == null) {
            str3 = "";
        } else {
            str3 = this.f1215c.mUnprovisionedMeshNodeData.getProductId() + "";
        }
        a.a.a.a.b.m.b.a("ALSMesh", "ble", str3, false, null, "", 0L, meshUtConst$MeshErrorEnum.getErrorCode(), meshUtConst$MeshErrorEnum.getErrorMsg(), str, str2);
        LocalBroadcastManager.getInstance(this.f1215c).sendBroadcast(intent);
    }
}
