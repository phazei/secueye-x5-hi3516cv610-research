package a.a.a.a.b;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import b.InterfaceC0367a;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.bean.MeshNodeStatus;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import datasource.MeshConfigCallback;
import meshprovisioner.states.UnprovisionedMeshNode;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class M implements MeshConfigCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ InterfaceC0367a.InterfaceC0176a f1216a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ MeshService f1217b;

    public M(MeshService meshService, InterfaceC0367a.InterfaceC0176a interfaceC0176a) {
        this.f1217b = meshService;
        this.f1216a = interfaceC0176a;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "provisionAuth request success " + bool);
        if (bool != null && this.f1216a != null && (this.f1217b.mCurrentProvisionMeshNode instanceof UnprovisionedMeshNode)) {
            this.f1216a.a((UnprovisionedMeshNode) this.f1217b.mCurrentProvisionMeshNode, bool.booleanValue());
            return;
        }
        String str = MeshService.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("callback is null ? ");
        sb.append(this.f1216a == null);
        sb.append(", mMeshNode == null ? ");
        sb.append(this.f1217b.mCurrentProvisionMeshNode == null);
        sb.append(", mMeshNode instanceof UnprovisionedMeshNode ? ");
        sb.append(this.f1217b.mCurrentProvisionMeshNode instanceof UnprovisionedMeshNode);
        a.a.a.a.b.m.a.b(str, sb.toString());
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        String str3;
        a.a.a.a.b.m.a.b(MeshService.TAG, "provisionAuth request failed, errorMessage: " + str2);
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_CONFIGURATION_STATE, MeshNodeStatus.REQUEST_FAILED.getState());
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.PROVISION_AUTH_REQUEST_ERROR;
        intent.putExtra(Utils.EXTRA_REQUEST_FAIL_MSG, meshUtConst$MeshErrorEnum.getErrorMsg() + " : " + str2);
        if (this.f1217b.mUnprovisionedMeshNodeData == null) {
            str3 = "";
        } else {
            str3 = this.f1217b.mUnprovisionedMeshNodeData.getProductId() + "";
        }
        a.a.a.a.b.m.b.a("ALSMesh", "ble", str3, false, null, "", 0L, meshUtConst$MeshErrorEnum.getErrorCode(), meshUtConst$MeshErrorEnum.getErrorMsg(), str, str2);
        LocalBroadcastManager.getInstance(this.f1217b).sendBroadcast(intent);
    }
}
