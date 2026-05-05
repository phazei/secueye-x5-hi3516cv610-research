package com.aliyun.alink.linksdk.tmp.device.payload.cloud;

import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;

/* JADX INFO: loaded from: classes2.dex */
public class EncryptGroupAuthInfo {
    public String encryptAccessKey;
    public String encryptAccessToken;
    public String encryptGroupKeyPrefix;
    public String encryptGroupSecret;

    public String toString() {
        super.toString();
        return GsonUtils.toJson(this);
    }
}
