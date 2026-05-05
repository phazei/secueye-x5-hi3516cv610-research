package com.alibaba.sdk.android.openaccount.security.impl;

import com.alibaba.sdk.android.openaccount.util.HmacSHA1Util;
import com.aliyun.alink.linksdk.securesigner.ISecuritySource;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;

/* JADX INFO: loaded from: classes.dex */
public class DefaultSecuritySourceImpl implements ISecuritySource {
    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String getAppKey() {
        return GlobalConfig.getInstance().getInitConfig().getAppKeyConfig().appKey;
    }

    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String sign(String str, String str2) {
        return HmacSHA1Util.hmacSha1(GlobalConfig.getInstance().getInitConfig().getAppKeyConfig().appSecret, str).toLowerCase();
    }
}
