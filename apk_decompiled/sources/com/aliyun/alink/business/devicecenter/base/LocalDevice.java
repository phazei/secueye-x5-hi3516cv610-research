package com.aliyun.alink.business.devicecenter.base;

import com.aliyun.alink.business.devicecenter.utils.StringUtils;

/* JADX INFO: loaded from: classes.dex */
public class LocalDevice {
    public String productKey = null;
    public String deviceName = null;
    public String type = "0";
    public String token = null;
    public String random = null;
    public String cipherType = null;
    public String ip = null;
    public String mac = null;
    public String service = null;
    public String fwVersion = null;

    public boolean equals(Object obj) {
        if (!(obj instanceof LocalDevice)) {
            return false;
        }
        LocalDevice localDevice = (LocalDevice) obj;
        return StringUtils.isEqualString(this.productKey, localDevice.productKey) && StringUtils.isEqualString(this.deviceName, localDevice.deviceName);
    }

    public String toString() {
        return "{\"ip\":\"" + this.ip + "\",\"type\":\"" + this.type + "\",\"productKey\":\"" + this.productKey + "\",\"deviceName\":\"" + this.deviceName + "\",\"random\":\"" + this.random + "\",\"cipherType\":\"" + this.cipherType + "\",\"mac\":\"" + this.mac + "\",\"token\":\"" + this.token + "\",\"service\":\"" + this.service + "\",\"fwVersion\":\"" + this.fwVersion + "\"}";
    }
}
