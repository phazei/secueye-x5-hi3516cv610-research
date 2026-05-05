package com.aliyun.alink.linksdk.tmp.config;

import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultClientConfig extends DeviceConfig {
    public String mAccessKey;
    public String mAccessToken;
    public int mConnectKeepType;
    public String mDateFormat;

    public DefaultClientConfig(DeviceBasicData deviceBasicData) {
        setBasicData(deviceBasicData);
        this.mDeviceType = DeviceConfig.DeviceType.CLIENT;
        this.mConnectKeepType = AlcsPalConst.CONNECT_KEEP_TYPE_DYNAMIC;
    }

    public String getAccessKey() {
        return this.mAccessKey;
    }

    public void setAccessKey(String str) {
        this.mAccessKey = str;
    }

    public String getAccessToken() {
        return this.mAccessToken;
    }

    public void setAccessToken(String str) {
        this.mAccessToken = str;
    }
}
