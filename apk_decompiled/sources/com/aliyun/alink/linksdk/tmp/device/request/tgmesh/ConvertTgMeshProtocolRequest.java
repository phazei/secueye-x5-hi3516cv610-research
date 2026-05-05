package com.aliyun.alink.linksdk.tmp.device.request.tgmesh;

import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ILpbsCloudProxy;
import com.aliyun.alink.linksdk.alcs.lpbs.data.cloud.TgMeshConvertResult;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;

/* JADX INFO: loaded from: classes2.dex */
public class ConvertTgMeshProtocolRequest extends GateWayRequest {
    public String deviceId;
    public String params;

    public static class ConvertTgMeshProtocolResponse extends GateWayResponse<TgMeshConvertResult> {
    }

    public ConvertTgMeshProtocolRequest() {
        this.path = ILpbsCloudProxy.TOPIC_CONVERT_TGMESH_PARAMS;
        this.version = "1.0.0";
        this.responseClass = ConvertTgMeshProtocolResponse.class;
    }
}
