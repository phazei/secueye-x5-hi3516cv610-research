package com.aliyun.alink.linksdk.cmp.connect.hubapi;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig;

/* JADX INFO: loaded from: classes2.dex */
public class HubApiClientConnectConfig extends AConnectConfig {
    public String domain = "iot.cn-shanghai.aliyuncs.com";
    public String productKey = null;
    public String deviceName = null;
    public String deviceSecret = null;
    public String productSecret = null;

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig
    public boolean checkVaild() {
        if (TextUtils.isEmpty(this.productKey) || TextUtils.isEmpty(this.deviceName)) {
            return false;
        }
        return (TextUtils.isEmpty(this.productSecret) && TextUtils.isEmpty(this.deviceSecret)) ? false : true;
    }
}
