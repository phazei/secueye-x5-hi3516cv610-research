package com.alibaba.sdk.android.oauth;

/* JADX INFO: loaded from: classes.dex */
public class AppCredential {
    public String appKey;
    public String appSecret;
    public String redirectUrl;

    public AppCredential() {
    }

    public AppCredential(String str, String str2) {
        this.appKey = str;
        this.appSecret = str2;
    }

    public AppCredential(String str, String str2, String str3) {
        this.appKey = str;
        this.appSecret = str2;
        this.redirectUrl = str3;
    }
}
