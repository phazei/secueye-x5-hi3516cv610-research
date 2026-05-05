package com.aliyun.alink.linksdk.alcs.lpbs.data;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
public class PalDeviceInfo {
    public String deviceId;
    public String ip;
    public String mac;
    public String productModel;

    public String getDevId() {
        if (TextUtils.isEmpty(this.productModel)) {
            return this.deviceId;
        }
        return this.productModel + this.deviceId;
    }

    public PalDeviceInfo(String str, String str2) {
        this(str, str2, null);
    }

    public PalDeviceInfo(String str, String str2, String str3) {
        this(str, str2, str3, null);
    }

    public PalDeviceInfo(String str, String str2, String str3, String str4) {
        this.productModel = str;
        this.deviceId = str2;
        this.ip = str3;
        this.mac = str4;
    }

    public String getPkDnChangeDevId(String str) {
        return getDevId() + str;
    }

    public String toString() {
        return this.productModel + this.deviceId;
    }
}
