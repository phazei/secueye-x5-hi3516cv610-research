package com.aliyun.alink.linksdk.tmp.device.payload.property;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class SetPropertyRequestPayload extends CommonRequestPayload<Map<String, ValueWrapper>> {
    /* JADX WARN: Type inference failed for: r1v2, types: [T, java.util.HashMap] */
    public SetPropertyRequestPayload(String str, String str2) {
        super(str, str2);
        setMethod("thing.service.property.set");
        this.params = new HashMap();
    }

    public Map<String, ValueWrapper> getProperty() {
        return (Map) this.params;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setProperty(Map<String, ValueWrapper> map) {
        this.params = map;
    }
}
