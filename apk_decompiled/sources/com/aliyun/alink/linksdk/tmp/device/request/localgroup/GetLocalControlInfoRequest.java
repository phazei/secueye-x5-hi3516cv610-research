package com.aliyun.alink.linksdk.tmp.device.request.localgroup;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;

/* JADX INFO: loaded from: classes2.dex */
public class GetLocalControlInfoRequest extends GateWayRequest {
    public String controlGroupId;

    public static class GetLocalControlInfoResponse extends GateWayResponse<GetLocalControlInfoData> {

        public static class GetLocalControlInfoData {
            public String accessKey;
            public String accessToken;
            public String localGroupId;
        }
    }

    public GetLocalControlInfoRequest() {
        super(GetLocalControlInfoResponse.class);
        this.path = "/living/home/controlgroup/localcontrolinfo/get";
    }
}
