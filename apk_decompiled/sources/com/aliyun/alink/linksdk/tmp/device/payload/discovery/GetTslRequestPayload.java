package com.aliyun.alink.linksdk.tmp.device.payload.discovery;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class GetTslRequestPayload extends CommonRequestPayload<Map<String, Object>> {
    /* JADX WARN: Type inference failed for: r1v1, types: [T, java.util.HashMap] */
    public GetTslRequestPayload(String str, String str2) {
        super(str, str2);
        this.params = new HashMap();
    }

    public void putIotId(String str) {
        ((Map) this.params).put("iotId", str);
    }
}
