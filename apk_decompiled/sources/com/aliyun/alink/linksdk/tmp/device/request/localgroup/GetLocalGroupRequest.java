package com.aliyun.alink.linksdk.tmp.device.request.localgroup;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;

/* JADX INFO: loaded from: classes2.dex */
public class GetLocalGroupRequest extends GateWayRequest {
    public String localGroupId;

    public static class GetLocalGroupResponse extends GateWayResponse<GetLocalGroupResData> {

        public static class GetLocalGroupResData {
            public String accessKey;
            public String accessToken;
            public String localGroupId;
        }
    }

    public GetLocalGroupRequest() {
        super(GetLocalGroupResponse.class);
        this.path = "/living/alcs/localgroup/get";
    }
}
