package com.aliyun.alink.linksdk.tmp.device.payload.cloud;

/* JADX INFO: loaded from: classes2.dex */
public class UpdatePrefixRequestPayload {
    public int id;
    public String method;
    public PrefixUpdateParams params;
    public String version;

    public static class PrefixUpdateParams {
        public String prefix;
    }
}
