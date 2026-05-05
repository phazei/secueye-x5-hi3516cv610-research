package com.aliyun.alink.linksdk.tmp.data.service;

import com.aliyun.alink.linksdk.tmp.utils.TextHelper;

/* JADX INFO: loaded from: classes2.dex */
public class DevTripleInfo {
    public String deviceName;
    public String deviceSecret;
    public String productKey;

    public String getId() {
        return TextHelper.combineStr(this.productKey, this.deviceName);
    }
}
