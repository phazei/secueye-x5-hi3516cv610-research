package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.GatewayMeshStrategy;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.facebook.internal.NativeProtocol;

/* JADX INFO: compiled from: GatewayMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class S implements IoTCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ GatewayMeshStrategy f3677a;

    public S(GatewayMeshStrategy gatewayMeshStrategy) {
        this.f3677a = gatewayMeshStrategy;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        this.f3677a.provisionErrorInfo = new DCErrorCode(NativeProtocol.ERROR_NETWORK_ERROR, DCErrorCode.PF_NETWORK_ERROR).setSubcode(DCErrorCode.SUBCODE_API_REQUEST_ON_FAILURE).setMsg("meshDeviceProvisionTrigger failed :" + exc);
        this.f3677a.provisionResultCallback(null);
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        if (this.f3677a.provisionHasStopped.get()) {
            return;
        }
        if (ioTResponse == null || ioTResponse.getCode() != 200 || ioTResponse.getData() == null) {
            this.f3677a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setMsg("mesh/gateway/provision/trigger request error").setSubcode(ioTResponse != null ? ioTResponse.getCode() : 0);
            this.f3677a.provisionResultCallback(null);
            return;
        }
        String string = ioTResponse.getData().toString();
        if (!TextUtils.isEmpty(string)) {
            this.f3677a.loopQueryMeshProvisionResult(string);
        } else {
            this.f3677a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setMsg("mesh/gateway/provision/trigger taskId is null").setSubcode(DCErrorCode.SUBCODE_SRE_MESH_GATEWAY_PROVISION_TRIGGER_RESPONSE_EMPTY);
            this.f3677a.provisionResultCallback(null);
        }
    }
}
