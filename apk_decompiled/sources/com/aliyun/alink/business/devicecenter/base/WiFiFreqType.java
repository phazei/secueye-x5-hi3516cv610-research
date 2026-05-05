package com.aliyun.alink.business.devicecenter.base;

import com.huawei.hms.framework.network.grs.GrsBaseInfo;

/* JADX INFO: loaded from: classes.dex */
public enum WiFiFreqType {
    WIFI_5G("5GHZ"),
    WIFI_2_4G("2.4GHZ"),
    WIFI_UNKNOWN(GrsBaseInfo.CountryCodeSource.UNKNOWN);

    public String name;

    WiFiFreqType(String str) {
        this.name = str;
    }

    public String value() {
        return this.name;
    }
}
