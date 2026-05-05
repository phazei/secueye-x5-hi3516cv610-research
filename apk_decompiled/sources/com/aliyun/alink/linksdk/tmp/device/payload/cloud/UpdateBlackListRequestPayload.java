package com.aliyun.alink.linksdk.tmp.device.payload.cloud;

/* JADX INFO: loaded from: classes2.dex */
public class UpdateBlackListRequestPayload {
    public int id;
    public String method;
    public UpdateBlackListParams params;
    public String version;

    public static class UpdateBlackListParams {
        public String blacklist;
    }
}
