package com.aliyun.alink.linksdk.alcs.coap;

/* JADX INFO: loaded from: classes2.dex */
public interface IAuthHandler {
    public static final int AUTH_ACCESS_TOKEN_INVALID = 502;
    public static final int AUTH_ACCESS_TOKEN_REVOKE = 501;
    public static final int AUTH_AUTH_ILLEGALSIGN = 506;
    public static final int AUTH_SUCCESS = 200;

    void onAuthResult(String str, int i, int i2);
}
