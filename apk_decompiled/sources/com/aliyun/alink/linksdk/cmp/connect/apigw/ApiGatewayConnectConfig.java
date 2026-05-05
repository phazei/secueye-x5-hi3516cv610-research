package com.aliyun.alink.linksdk.cmp.connect.apigw;

import com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig;

/* JADX INFO: loaded from: classes2.dex */
public class ApiGatewayConnectConfig extends AConnectConfig {
    public String appkey;
    public String host = "api.link.aliyun.com";
    public String securityGuardAuthcode;

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig
    public boolean checkVaild() {
        return true;
    }
}
