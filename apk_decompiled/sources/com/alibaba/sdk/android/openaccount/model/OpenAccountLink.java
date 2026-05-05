package com.alibaba.sdk.android.openaccount.model;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountLink {
    public String avatarUrl;
    public String deviceId;
    public Integer gender = 0;
    public String nickName;
    public String openAccountId;
    public String outerId;
    public String outerPlatform;
    public String type;
    public Boolean useLogin;

    public String toString() {
        return "deviceId: " + this.deviceId + ",nickName: " + this.nickName + ", openAccountId: " + this.openAccountId + ",outerId: " + this.outerId + ", outerPlatform: " + this.outerPlatform + ", type: " + this.type + ",useLogin: " + this.useLogin + ", avatarUrl:  " + this.avatarUrl + ", gender: " + this.gender;
    }
}
