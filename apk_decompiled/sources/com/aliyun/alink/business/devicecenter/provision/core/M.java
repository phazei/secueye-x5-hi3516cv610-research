package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentGateMeshStrategy;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;
import com.facebook.internal.NativeProtocol;
import java.util.List;

/* JADX INFO: compiled from: ConcurrentGateMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class M extends ApiCallBack<List<String>> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ConcurrentGateMeshStrategy f3672a;

    public M(ConcurrentGateMeshStrategy concurrentGateMeshStrategy) {
        this.f3672a = concurrentGateMeshStrategy;
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(List<String> list) {
        if (this.f3672a.provisionHasStopped.get()) {
            return;
        }
        this.f3672a.mTaskIds = list;
        if (this.f3672a.mTaskIds == null || this.f3672a.mTaskIds.size() == 0) {
            this.f3672a.allGateMeshProvisionFail(new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setMsg("/living/awss/bt/mesh/gateway/provision/devices/trigger taskId is null").setSubcode(DCErrorCode.SUBCODE_SRE_MESH_GATEWAY_PROVISION_TRIGGER_RESPONSE_EMPTY));
        } else {
            this.f3672a.loopQueryMeshProvisionResult();
        }
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onFail(int i, String str) {
        ALog.d(ConcurrentGateMeshStrategy.TAG, "onFailure() called with: ioTRequest = [batchMeshDeviceProvisionTrigger], e = [" + str + "]");
        this.f3672a.allGateMeshProvisionFail(new DCErrorCode(NativeProtocol.ERROR_NETWORK_ERROR, DCErrorCode.PF_NETWORK_ERROR).setSubcode(DCErrorCode.SUBCODE_API_REQUEST_ON_FAILURE).setMsg("meshDeviceProvisionTrigger failed :" + str));
    }
}
