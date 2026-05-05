package com.aliyun.alink.linksdk.tmp.device.request.auth;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;

/* JADX INFO: loaded from: classes2.dex */
public class GetByAccountAndDevRequest extends GateWayRequest {
    public String iotId;

    public static class GetByAccountAndDevData {
        public String deviceName;
    }

    public static class GetByAccountAndDevResponse extends GateWayResponse<GetByAccountAndDevData> {
    }

    public GetByAccountAndDevRequest(String str) {
        super(GetByAccountAndDevResponse.class);
        this.path = "/uc/getByAccountAndDev";
        this.version = "1.0.2";
        this.iotId = str;
    }
}
