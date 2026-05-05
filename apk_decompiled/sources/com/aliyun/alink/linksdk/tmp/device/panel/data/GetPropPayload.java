package com.aliyun.alink.linksdk.tmp.device.panel.data;

import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class GetPropPayload {
    public Map<String, PropItem> data = new HashMap();

    public static class PropItem {
        public long time;
        public ValueWrapper value;

        public PropItem(long j, ValueWrapper valueWrapper) {
            this.value = valueWrapper;
            this.time = j;
        }
    }
}
