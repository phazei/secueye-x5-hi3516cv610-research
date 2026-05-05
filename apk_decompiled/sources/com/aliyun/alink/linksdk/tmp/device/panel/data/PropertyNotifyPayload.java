package com.aliyun.alink.linksdk.tmp.device.panel.data;

import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class PropertyNotifyPayload {
    public String method;
    public PropertyParams params;

    public static class ItemData {
        public String time;
        public ValueWrapper value;
    }

    public static class PropertyParams {
        public String deviceName;
        public String iotId;
        public Map<String, ItemData> items;
        public String productKey;
    }
}
