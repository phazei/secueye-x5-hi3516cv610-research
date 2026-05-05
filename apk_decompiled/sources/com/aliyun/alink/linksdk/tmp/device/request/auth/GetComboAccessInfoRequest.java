package com.aliyun.alink.linksdk.tmp.device.request.auth;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;

/* JADX INFO: loaded from: classes2.dex */
public class GetComboAccessInfoRequest extends GateWayRequest {
    public String deviceName;
    public String iotId;
    public String productKey;
    public boolean reissue;

    public static class AccessInfo {
        public String accessKey;
        public String accessToken;
    }

    public static class GetComboAccessInfoData {
        public AccessInfo accessInfo;
        public syncAccessInfo syncAccessInfo;
    }

    public static class GetComboAccessInfoResponse extends GateWayResponse<GetComboAccessInfoData> {
    }

    public static class syncAccessInfo extends AccessInfo {
        public String authCode;
    }

    public GetComboAccessInfoRequest(String str, String str2, String str3) {
        super(GetComboAccessInfoResponse.class);
        this.path = "/living/device/localcontrol/accessinfo/get";
        this.productKey = str;
        this.deviceName = str2;
        this.iotId = str3;
        this.reissue = false;
    }
}
