package com.aliyun.alink.linksdk.tmp.device.request.other;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class GetDeviceNetTypesSupportedRequest extends GateWayRequest {
    public String iotId;
    public String productKey;

    public static class GetDeviceNetTypesSupportedResponse extends GateWayResponse<List<String>> {
    }

    public GetDeviceNetTypesSupportedRequest(String str, String str2) {
        super(GetDeviceNetTypesSupportedResponse.class);
        this.path = "/living/device/net/type/get";
        this.productKey = str;
        this.iotId = str2;
    }
}
