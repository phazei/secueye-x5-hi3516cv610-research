package com.aliyun.alink.linksdk.alcs.data.ica;

/* JADX INFO: loaded from: classes2.dex */
public class ICAAuthPairs {
    public ICAAuthParams authParams = new ICAAuthParams();
    public ICAAuthServerParams authServerParams;

    public ICAAuthPairs(String str, String str2, String str3, String str4) {
        ICAAuthParams iCAAuthParams = this.authParams;
        iCAAuthParams.accessKey = str;
        iCAAuthParams.accessToken = str2;
        this.authServerParams = new ICAAuthServerParams();
        ICAAuthServerParams iCAAuthServerParams = this.authServerParams;
        iCAAuthServerParams.authCode = str3;
        iCAAuthServerParams.authSecret = str4;
    }
}
