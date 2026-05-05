package com.aliyun.alink.linksdk.tmp.data.auth;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AuthenRegisterReqPayload {
    public int id;
    public String method;
    public List<AuthenRegisterParams> params;
    public String version;

    public static class AuthenRegisterParams {
        public String deviceName;
        public String productKey;

        public AuthenRegisterParams(String str, String str2) {
            this.productKey = str;
            this.deviceName = str2;
        }
    }
}
