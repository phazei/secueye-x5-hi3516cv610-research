package com.aliyun.alink.linksdk.tmp.device.request.auth;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;

/* JADX INFO: loaded from: classes2.dex */
public class NotifyAccessInfoRequest extends GateWayRequest {
    public String deviceName;
    public String iotId;
    public String productKey;

    public static class NotifyAccessInfoResponse extends GateWayResponse<Boolean> {
    }

    public NotifyAccessInfoRequest(String str, String str2, String str3) {
        super(NotifyAccessInfoResponse.class);
        this.path = "/living/device/localcontrol/accessinfo/sync/notify";
        this.productKey = str;
        this.deviceName = str2;
        this.iotId = str3;
    }
}
