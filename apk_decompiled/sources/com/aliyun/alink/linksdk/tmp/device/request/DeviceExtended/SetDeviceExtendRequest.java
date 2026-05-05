package com.aliyun.alink.linksdk.tmp.device.request.DeviceExtended;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;

/* JADX INFO: loaded from: classes2.dex */
public class SetDeviceExtendRequest extends GateWayRequest {
    public String dataKey;
    public String dataValue;
    public String iotId;

    public SetDeviceExtendRequest(String str, String str2, String str3) {
        this.path = "/thing/extended/property/set";
        this.version = "1.0.2";
        this.iotId = str;
        this.dataKey = str2;
        this.dataValue = str3;
    }
}
