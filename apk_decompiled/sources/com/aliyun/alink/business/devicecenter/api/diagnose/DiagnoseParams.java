package com.aliyun.alink.business.devicecenter.api.diagnose;

/* JADX INFO: loaded from: classes.dex */
public class DiagnoseParams {
    public String deviceSSID = null;
    public int timeout = 60;
    public String productKey = null;
    public String deviceName = null;

    public String toString() {
        return "{\"deviceSSID\":\"" + this.deviceSSID + "\",\"timeout\":\"" + this.timeout + "\",\"productKey\":\"" + this.productKey + "\",\"deviceName\":\"" + this.deviceName + "\"}";
    }
}
