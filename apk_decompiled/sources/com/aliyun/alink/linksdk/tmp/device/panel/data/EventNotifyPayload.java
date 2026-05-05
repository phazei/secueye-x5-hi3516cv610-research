package com.aliyun.alink.linksdk.tmp.device.panel.data;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;

/* JADX INFO: loaded from: classes2.dex */
public class EventNotifyPayload {
    public String method;
    public EventParams params;

    public static class EventParams {
        public String deviceName;
        public String identifier;
        public String iotId;
        public String name;
        public String productKey;
        public long time;
        public String type;
        public OutputParams value;
    }
}
