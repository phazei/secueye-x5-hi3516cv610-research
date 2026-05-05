package com.aliyun.alink.linksdk.tmp.device.request.DeviceExtended;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;

/* JADX INFO: loaded from: classes2.dex */
public class GetDeviceExtendRequest extends GateWayRequest {
    public String dataKey;
    public String iotId;

    public static class DeviceExtendGetResponse extends GateWayResponse<String> {
    }

    public GetDeviceExtendRequest(String str, String str2) {
        super(DeviceExtendGetResponse.class);
        this.path = "/living/thing/extended/property/get";
        this.version = "1.0.0";
        this.iotId = str;
        this.dataKey = str2;
    }
}
