package com.aliyun.alink.linksdk.tmp.device.panel.data;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AccessInfoPayload {
    public int code;
    public List<AlcsDeviceInfo> data;
    public String id;

    public static class AlcsDeviceInfo {
        public String accessKey;
        public String accessToken;
        public String deviceName;
        public String iotId;
        public String productKey;
    }
}
