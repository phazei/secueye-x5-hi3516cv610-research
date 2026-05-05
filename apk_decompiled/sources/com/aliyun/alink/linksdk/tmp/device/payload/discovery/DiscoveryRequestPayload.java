package com.aliyun.alink.linksdk.tmp.device.payload.discovery;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.google.gson.JsonObject;

/* JADX INFO: loaded from: classes2.dex */
public class DiscoveryRequestPayload extends CommonRequestPayload {
    /* JADX WARN: Type inference failed for: r0v1, types: [T, com.google.gson.JsonObject] */
    public DiscoveryRequestPayload() {
        super(null, null);
        this.params = new JsonObject();
    }
}
