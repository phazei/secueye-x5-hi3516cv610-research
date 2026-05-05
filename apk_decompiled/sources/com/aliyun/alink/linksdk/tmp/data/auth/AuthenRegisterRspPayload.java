package com.aliyun.alink.linksdk.tmp.data.auth;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AuthenRegisterRspPayload {
    public int code;
    public List<AuthenRegisterRspData> data;
    public int id;

    public static class AuthenRegisterRspData {
        public String deviceName;
        public String deviceSecret;
        public String productKey;
    }
}
