package com.aliyun.alink.linksdk.tmp.device.payload.service;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class ServiceRequestPayload extends CommonRequestPayload<Map<String, ValueWrapper>> {
    /* JADX WARN: Type inference failed for: r1v1, types: [T, java.util.HashMap] */
    public ServiceRequestPayload(String str, String str2) {
        super(str, str2);
        this.params = new HashMap();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload
    public void setParams(Map<String, ValueWrapper> map) {
        this.params = map;
    }
}
