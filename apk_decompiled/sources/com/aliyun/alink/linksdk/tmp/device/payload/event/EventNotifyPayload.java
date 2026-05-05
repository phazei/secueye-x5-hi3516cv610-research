package com.aliyun.alink.linksdk.tmp.device.payload.event;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class EventNotifyPayload extends CommonRequestPayload<Map<String, ValueWrapper>> {
    /* JADX WARN: Type inference failed for: r1v1, types: [T, java.util.HashMap] */
    public EventNotifyPayload(String str, String str2) {
        super(str, str2);
        this.params = new HashMap();
    }
}
