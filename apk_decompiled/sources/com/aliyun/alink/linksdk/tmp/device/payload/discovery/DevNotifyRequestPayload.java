package com.aliyun.alink.linksdk.tmp.device.payload.discovery;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.discovery.DiscoveryResponsePayload;

/* JADX INFO: loaded from: classes2.dex */
public class DevNotifyRequestPayload extends CommonRequestPayload<DiscoveryResponsePayload.DiscoveryResponseData> {
    /* JADX WARN: Type inference failed for: r0v1, types: [T, com.aliyun.alink.linksdk.tmp.device.payload.discovery.DiscoveryResponsePayload$DiscoveryResponseData] */
    public DevNotifyRequestPayload() {
        super(null, null);
        this.params = new DiscoveryResponsePayload.DiscoveryResponseData();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setDeviceModel(String str) {
        ((DiscoveryResponsePayload.DiscoveryResponseData) this.params).setDeviceModel(str);
    }
}
