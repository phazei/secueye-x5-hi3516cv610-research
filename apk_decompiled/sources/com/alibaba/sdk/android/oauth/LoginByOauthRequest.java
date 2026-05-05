package com.alibaba.sdk.android.oauth;

/* JADX INFO: loaded from: classes.dex */
public class LoginByOauthRequest {
    public String accessToken;
    public String authCode;
    public String oauthAppKey;
    public int oauthPlateform;
    public String openId;
    public String tokenType;
    public String userData;

    public String toString() {
        return "LoginByOauthRequest{accessToken='" + this.accessToken + "', openId='" + this.openId + "', oauthAppKey='" + this.oauthAppKey + "', oauthPlateform=" + this.oauthPlateform + ", tokenType='" + this.tokenType + "', userData=" + this.userData + "'}";
    }
}
